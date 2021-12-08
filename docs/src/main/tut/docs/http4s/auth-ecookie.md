---
layout: docs
number: 7
title: "Encrypted cookie auth"
---
# Encrypted cookie authentication

Encrypted cookie authenticator uses `TsecCookieSettings` for configuration:
```scala
  final case class TSecCookieSettings(
      cookieName: String = "tsec-auth-cookie",
      secure: Boolean,
      httpOnly: Boolean = true,
      domain: Option[String] = None,
      path: Option[String] = None,
      extension: Option[String] = None,
      expiryDuration: FiniteDuration,
      maxIdle: Option[FiniteDuration]
  )
```

And for backing store, in the non-stateless case,  `AuthEncryptedCookie`:

```scala

final case class AuthEncryptedCookie[A, Id](
    id: UUID, //The cookie id
    name: String, //Cookie name. This is what the cookie will be sent as to your client
    content: AEADCookie[A], //The actual cookie content. This is a newtype over string. Coerce using `AEADCookie[A](rawString)`
    messageId: Id, //Your user Id type. I.e for our example user class, this is Int
    expiry: HttpDate, //The expiry time, as an HttpDate compatible with http4s
    lastTouched: Option[HttpDate], //Rolling window expiration time last touched.
    secure: Boolean, //TLS only?
    httpOnly: Boolean = true,
    domain: Option[String] = None,
    path: Option[String] = None,
    extension: Option[String] = None
)
```

This authenticator uses cookies as the underlying mechanism to track state, however, any information such as expiry, 
rolling window expiration or id is encrypted, as well as signed (AEAD encryption). 
This authenticator has both stateful and stateless (however users are currently checked with the backend).

* Choose between one of AES128, AES192 or AES256 to perform your Authenticated Encryption with AES-GCM. 
**Recommended default: AES128**.
* User and token backing store as stated above, or just User store for stateless authenticator
* Can be vulnerable to [CSRF](https://en.wikipedia.org/wiki/Cross-site_request_forgery), to be used with the CSRF middleware.
* [CORS](https://en.wikipedia.org/wiki/Cross-origin_resource_sharing) doesn't play nice with cookies.
* Your ID type for your user must have an `Encoder` and `Decoder` instance from circe

### Authenticator creation
If want want to create services, create a request handler as such:

```tut:silent
import java.util.UUID

import cats.effect.IO
import org.http4s.HttpService
import org.http4s.dsl.io._
import tsec.authentication._
import tsec.cipher.symmetric.jca._

import scala.concurrent.duration._

object EncryptedCookieExample {

  import http4sExamples.ExampleAuthHelpers._

  implicit val encryptor   = AES128GCM.genEncryptor[IO]
  implicit val gcmstrategy = AES128GCM.defaultIvStrategy[IO]

  val cookieBackingStore: BackingStore[IO, UUID, AuthEncryptedCookie[AES128GCM, Int]] =
    dummyBackingStore[IO, UUID, AuthEncryptedCookie[AES128GCM, Int]](_.id)

  // We create a way to store our users. You can attach this to say, your doobie accessor
  val userStore: BackingStore[IO, Int, User] = dummyBackingStore[IO, Int, User](_.id)

  val settings: TSecCookieSettings = TSecCookieSettings(
    cookieName = "tsec-auth",
    secure = false,
    expiryDuration = 10.minutes, // Absolute expiration time
    maxIdle = None // Rolling window expiration. Set this to a FiniteDuration if you intend to have one
  )

  val key: SecretKey[AES128GCM] = AES128GCM.unsafeGenerateKey //Our encryption key

  val authWithBackingStore = //Instantiate a stateful authenticator
    EncryptedCookieAuthenticator.withBackingStore(
      settings,
      cookieBackingStore,
      userStore,
      key
    )

  val stateless = //Instantiate a stateless authenticator
    EncryptedCookieAuthenticator.stateless(
      settings,
      userStore,
      key
    )

  val Auth =
    SecuredRequestHandler(stateless)

  /*
  Now from here, if want want to create services, we simply use the following
  (Note: Since the type of the service is HttpService[IO], we can mount it like any other endpoint!):
   */
  val service: HttpService[IO] = Auth.liftService(TSecAuthService {
    //Where user is the case class User above
    case request @ GET -> Root / "api" asAuthed user =>
      /*
      Note: The request is of type: SecuredRequest, which carries:
      1. The request
      2. The Authenticator (i.e token)
      3. The identity (i.e in this case, User)
       */
      val r: SecuredRequest[IO, User, AuthEncryptedCookie[AES128GCM, Int]] = request
      Ok()
  })

}
```