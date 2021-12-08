---
layout: home
title:  "Home"
section: "home"
technologies:
 - first: ["Scala", "sbt-microsites plugin is completely written in Scala"]
 - second: ["SBT", "sbt-microsites plugin uses SBT and other sbt plugins to generate microsites easily"]
 - third: ["Jekyll", "Jekyll allows for the transformation of plain text into static websites and blogs."]
---

# TSec - Tagless Security

**TSec** Is a type-safe general cryptography library on the JVM.

[![Join the chat at https://gitter.im/tsecc/Lobby](https://badges.gitter.im/tsecc/Lobby.svg)](https://gitter.im/tsecc/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://travis-ci.org/jmcardon/tsec.svg?branch=master)](https://travis-ci.org/jmcardon/tsec)
[ ![Latest Version](https://maven-badges.herokuapp.com/maven-central/io.github.jmcardon/tsec-common_2.12/badge.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A"io.github.jmcardon"%20tsec)


For the current progress, please refer to the [RoadMap](https://github.com/jmcardon/tsec/wiki).

For version changes and additions, including breaking changes, see either [release notes](https://github.com/jmcardon/tsec/releases)
or the [Version Changes](https://github.com/jmcardon/tsec/wiki/Version-Changes) page.

### Note on milestones:
Our Notation for versions is:
```
X.X.X
^ ^ ^____Minor
| |______Major
|________Complete redesign (i.e scalaz 7 vs 8)  
```

All `x.x.x-Mx` releases are milestone releases. Thus, we do not guarantee binary compatibility or no api-breakage until
a concrete version(i.e `0.0.1`). We aim to keep userland-apis relatively stable, but 
internals shift as we find better/more performant abstractions.

We will guarantee compatibility between minor versions (i.e 0.0.1 => 0.0.2) but not major versions (0.0.1 => 0.1.0)

0.0.1-M11 is here for scala 2.12+ and Cats 1.0.1!

To get started, if you are on sbt 0.13.16+, add


| Name                  | Description                                              | Examples |
| -----                 | ----------                                               | -------- |
| tsec-common           | Common crypto utilities                                  |          |
| tsec-password         | Password hashers: BCrypt and Scrypt                      | [here](https://github.com/jmcardon/tsec/blob/master/examples/src/main/scala/PasswordHashingExamples.scala)|
| tsec-cipher-jca       | Symmetric encryption utilities                           | [here](https://github.com/jmcardon/tsec/blob/master/examples/src/main/scala/SymmetricCipherExamples.scala)|
| tsec-cipher-bouncy    | Symmetric encryption utilities                           | [here](https://github.com/jmcardon/tsec/blob/master/examples/src/main/scala/SymmetricCipherExamples.scala)|
| tsec-mac              | Message Authentication                                   | [here](https://github.com/jmcardon/tsec/blob/master/examples/src/main/scala/MacExamples.scala)|
| tsec-signatures       | Digital signatures                                       | [here](https://github.com/jmcardon/tsec/blob/master/examples/src/main/scala/SignatureExamples.scala)|
| tsec-hash-jca         | Message Digests (Hashing)                                | [here](https://github.com/jmcardon/tsec/blob/master/examples/src/main/scala/MessageDigestExamples.scala)|
| tsec-hash-bouncy      | Message Digests (Hashing)                                | [here](https://github.com/jmcardon/tsec/blob/master/examples/src/main/scala/MessageDigestExamples.scala)|
| tsec-libsodium        | Nicely-typed Libsodium JNI bridge                        | [here](https://github.com/jmcardon/tsec/blob/master/examples/src/main/scala/MessageDigestExamples.scala)|
| tsec-jwt-mac          | JWT implementation for Message Authentication signatures | [here](https://github.com/jmcardon/tsec/blob/master/examples/src/main/scala/JWTMacExamples.scala)|
| tsec-jwt-sig          | JWT implementation for Digital signatures                | [here](https://github.com/jmcardon/tsec/blob/master/examples/src/main/scala/JWTSignatureExamples.scala)|
| tsec-http4s           | Http4s Request Authentication and Authorization          | [here](https://github.com/jmcardon/tsec/tree/master/examples/src/main/scala/http4sExamples)|

To include any of these packages in your project use:

```scala
val tsecV = "0.0.1-M11"
 libraryDependencies ++= Seq(
 "io.github.jmcardon" %% "tsec-common" % tsecV,
 "io.github.jmcardon" %% "tsec-password" % tsecV,
 "io.github.jmcardon" %% "tsec-cipher-jca" % tsecV,
 "io.github.jmcardon" %% "tsec-cipher-bouncy" % tsecV,
 "io.github.jmcardon" %% "tsec-mac" % tsecV,
 "io.github.jmcardon" %% "tsec-signatures" % tsecV,
 "io.github.jmcardon" %% "tsec-hash-jca" % tsecV,
 "io.github.jmcardon" %% "tsec-hash-bouncy" % tsecV,
 "io.github.jmcardon" %% "tsec-libsodium" % tsecV,
 "io.github.jmcardon" %% "tsec-jwt-mac" % tsecV,
 "io.github.jmcardon" %% "tsec-jwt-sig" % tsecV,
 "io.github.jmcardon" %% "tsec-http4s" % tsecV
)
```

## Testing:

All tests can be run locally.

## Inspirations:

[play-silhouette](https://github.com/mohiva/play-silhouette)

[JCA](http://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html)

[Bouncy Castle](http://www.bouncycastle.org/)

[jwt-scala](https://github.com/pauldijou/jwt-scala)

## Big Thank you to our contributors (direct or indirect):
[Robert Soeldner](https://github.com/rsoeldner) (Contributor/Maintainer)

[Christopher Davenport](https://github.com/ChristopherDavenport)(Contributor/Maintainer)

[Harrison Houghton](https://github.com/hrhino)(Contributor/Maintainer)

[Bjørn Madsen](https://github.com/aeons) (Contributor)

[André Rouél](https://github.com/before)(Contributor)

[Edmund Noble](https://github.com/edmundnoble) (For the dank tagless)

[Fabio Labella](https://github.com/systemfw) (For the great FP help)

[Will Sargent](https://github.com/wsargent) (Security Discussions)
