package tsec.mac.libsodium

import tsec.libsodium.ScalaSodium
import tsec.mac._

sealed trait HS256

object HS256 extends SodiumMacPlatform[HS256]("HS256") {
  val keyLen: Int       = ScalaSodium.crypto_auth_hmacsha256_KEYBYTES
  val macLen: Int       = ScalaSodium.crypto_auth_hmacsha256_BYTES
  val algorithm: String = "HMACSHA256"

  private[tsec] def sodiumSign(in: Array[Byte], out: Array[Byte], key: SodiumMACKey[HS256])(
      implicit S: ScalaSodium
  ): Int = S.crypto_auth_hmacsha256(out, in, in.length, key)

  private[tsec] def sodiumVerify(in: Array[Byte], hashed: MAC[HS256], key: SodiumMACKey[HS256])(
      implicit S: ScalaSodium
  ): Int = S.crypto_auth_hmacsha256_verify(hashed, in, in.length, key)
}
