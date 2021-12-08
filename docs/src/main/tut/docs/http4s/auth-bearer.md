---
layout: docs
number: 10
title: "Bearer Token Authenticator"
---

# Bearer Token Authenticator

The bearer token authenticator uses `TSecTokenSettings` for configuration:

```scala
  final case class TSecTokenSettings(
      expirationTime: FiniteDuration,
      maxIdle: Option[FiniteDuration]
  )
```

And for token storage, it uses `TSecBearerToken` for storage:

```scala
final case class TSecBearerToken[I](
    id: SecureRandomId,             //Your secure random Id
    identity: I,                   //Your user ID type. in the case of our example, User has id type Int.
    expiry: Instant,              //The absolute expiration time
    lastTouched: Option[Instant] //Possible rolling window expiration
) extends Authenticator[I]
```

This authenticator uses a `SecureRandomId` (A 32-bit Id generated with a secure random number generator) as a bearer
 token to authenticate with information held server-side.
 
Notes:
* Not vulnerable to [CSRF](https://en.wikipedia.org/wiki/Cross-site_request_forgery).
* Okay to use with `CORS`
* Requires synchronized backing store.

### Authenticator creation

```tut:silent
import cats.effect.IO
import org.http4s.HttpService
import org.http4s.dsl.io._
import tsec.authentication._
import tsec.common.SecureRandomId

import concurrent.duration._

object BearerTokenExample {

  import http4sExamples.ExampleAuthHelpers._

  val bearerTokenStore =
    dummyBackingStore[IO, SecureRandomId, TSecBearerToken[Int]](s => SecureRandomId.coerce(s.id))

  //We create a way to store our users. You can attach this to say, your doobie accessor
  val userStore: BackingStore[IO, Int, User] = dummyBackingStore[IO, Int, User](_.id)

  val settings: TSecTokenSettings = TSecTokenSettings(
    expiryDuration = 10.minutes, //Absolute expiration time
    maxIdle = None
  )

  val bearerTokenAuth =
    BearerTokenAuthenticator(
      bearerTokenStore,
      userStore,
      settings
    )

  val Auth =
    SecuredRequestHandler(bearerTokenAuth)

  val authservice: TSecAuthService[TSecBearerToken[Int], User, IO] = TSecAuthService {
    case GET -> Root asAuthed user =>
      Ok()
  }

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
      val r: SecuredRequest[IO, User, TSecBearerToken[Int]] = request
      Ok()
  })
}
```