---
layout: docs
number: 8
title: "Signed Cookie Authentication"
---
# Signed cookie authentication

Signed cookie authenticator uses `TsecCookieSettings` for configuration:

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

And for your backing store, you need to store:

```scala
final case class AuthenticatedCookie[A, Id](
    id: UUID, //Cookie id
    name: String, //Cookie name, it will be sent to the client with this name
    content: SignedCookie[A], //Cookie name, it will be sent to the client with this name
    identity: Id,
    expiry: Instant,
    lastTouched: Option[Instant],
    secure: Boolean,
    httpOnly: Boolean = true,
    domain: Option[String] = None,
    path: Option[String] = None,
    extension: Option[String] = None
)
```

This authenticator uses cookies as the underlying mechanism to track state. If your particular Id type is sensitive,
_do not_ use this: the information is not encrypted. This is not a stateless authenticator.

Notes:
* Choose between one of HMACSHA1, HMACSHA256, HMACSHA384 or HMACSHA512. **Recommended: HMACSHA256.** The main difference between
all of these algorithms primarily lies in the difficulty to brute force the key: Higher number means higher search space, thus
harder to simply brute force the key.
* Can be vulnerable to [CSRF](https://en.wikipedia.org/wiki/Cross-site_request_forgery).
* [CORS](https://en.wikipedia.org/wiki/Cross-origin_resource_sharing) doesn't play nice with cookies.
* User and token backing store as stated above
* Your ID type for your user must have an `Encoder` and `Decoder` instance from circe

### Authenticator Creation

```tut:silent
import java.util.UUID

import cats.Id
import cats.effect.IO
import org.http4s.HttpService
import org.http4s.dsl.io._
import tsec.authentication._
import tsec.mac.jca.{HMACSHA256, MacSigningKey}
import scala.concurrent.duration._

object SignedCookieExample {

  import http4sExamples.ExampleAuthHelpers._

  val cookieBackingStore: BackingStore[IO, UUID, AuthenticatedCookie[HMACSHA256, Int]] =
    dummyBackingStore[IO, UUID, AuthenticatedCookie[HMACSHA256, Int]](_.id)

  //We create a way to store our users. You can attach this to say, your doobie accessor
  val userStore: BackingStore[IO, Int, User] = dummyBackingStore[IO, Int, User](_.id)

  val settings: TSecCookieSettings = TSecCookieSettings(
    cookieName = "tsec-auth",
    secure = false,
    expiryDuration = 10.minutes, // Absolute expiration time
    maxIdle = None // Rolling window expiration. Set this to a Finiteduration if you intend to have one
  )

  val key: MacSigningKey[HMACSHA256] = HMACSHA256.generateKey[Id] //Our Signing key. Instantiate in a safe way using GenerateLift

  val cookieAuth =
    SignedCookieAuthenticator(
      settings,
      cookieBackingStore,
      userStore,
      key
    )

  val Auth =
    SecuredRequestHandler(cookieAuth)

  /*
  Now from here, if want want to create services, we simply use the following
  (Note: Since the type of the service is HttpService[IO], we can mount it like any other endpoint!):
   */
  val service: HttpService[IO] = Auth.liftService(TSecAuthService {
    //Where user is the case class User above
    case request@GET -> Root / "api" asAuthed user =>
      /*
      Note: The request is of type: SecuredRequest, which carries:
      1. The request
      2. The Authenticator (i.e token)
      3. The identity (i.e in this case, User)
       */
      val r: SecuredRequest[IO, User, AuthenticatedCookie[HMACSHA256, Int]] = request
      Ok()
  })

}
```