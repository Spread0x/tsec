package tsec.cipher.symmetric.libsodium

import tsec.cipher.symmetric._
import tsec.cipher.symmetric.libsodium.internal._
import tsec.libsodium.ScalaSodium
import tsec.libsodium.ScalaSodium.{NullPtrBytes, NullPtrInt}

sealed trait XChacha20AEAD

object XChacha20AEAD extends SodiumAEADPlatform[XChacha20AEAD] {
  def algorithm: String = "XChacha20Poly1305IETF"

  val nonceLen: Int   = ScalaSodium.crypto_aead_xchacha20poly1305_ietf_NPUBBYTES
  val authTagLen: Int = ScalaSodium.crypto_aead_xchacha20poly1305_ietf_ABYTES
  val keyLength: Int  = ScalaSodium.crypto_aead_xchacha20poly1305_ietf_KEYBYTES

  private[tsec] def sodiumEncrypt(
      cout: Array[Byte],
      pt: PlainText,
      nonce: Array[Byte],
      key: SodiumKey[XChacha20AEAD]
  )(implicit S: ScalaSodium): Int =
    S.crypto_aead_xchacha20poly1305_ietf_encrypt(
      cout,
      NullPtrInt,
      pt,
      pt.length,
      NullPtrBytes,
      0,
      NullPtrBytes,
      nonce,
      key
    )

  private[tsec] def sodiumDecrypt(
      origOut: Array[Byte],
      ct: CipherText[XChacha20AEAD],
      key: SodiumKey[XChacha20AEAD]
  )(implicit S: ScalaSodium): Int =
    S.crypto_aead_xchacha20poly1305_ietf_decrypt(
      origOut,
      NullPtrInt,
      NullPtrBytes,
      ct.content,
      ct.content.length,
      NullPtrBytes,
      0,
      ct.nonce,
      key
    )

  private[tsec] def sodiumEncryptDetached(
      cout: Array[Byte],
      tagOut: Array[Byte],
      pt: PlainText,
      nonce: Array[Byte],
      key: SodiumKey[XChacha20AEAD]
  )(implicit S: ScalaSodium): Int =
    S.crypto_aead_xchacha20poly1305_ietf_encrypt_detached(
      cout,
      tagOut,
      NullPtrInt,
      pt,
      pt.length,
      NullPtrBytes,
      0,
      NullPtrBytes,
      nonce,
      key
    )

  private[tsec] def sodiumDecryptDetached(
      origOut: Array[Byte],
      ct: CipherText[XChacha20AEAD],
      tagIn: AuthTag[XChacha20AEAD],
      key: SodiumKey[XChacha20AEAD]
  )(implicit S: ScalaSodium): Int =
    S.crypto_aead_xchacha20poly1305_ietf_decrypt_detached(
      origOut,
      NullPtrBytes,
      ct.content,
      ct.content.length,
      tagIn,
      NullPtrBytes,
      0,
      ct.nonce,
      key
    )

  private[tsec] def sodiumEncryptAAD(
      cout: Array[Byte],
      pt: PlainText,
      nonce: Array[Byte],
      key: SodiumKey[XChacha20AEAD],
      aad: AAD
  )(implicit S: ScalaSodium): Int =
    S.crypto_aead_xchacha20poly1305_ietf_encrypt(
      cout,
      NullPtrInt,
      pt,
      pt.length,
      aad,
      aad.length,
      NullPtrBytes,
      nonce,
      key
    )

  private[tsec] def sodiumDecryptAAD(
      origOut: Array[Byte],
      ct: CipherText[XChacha20AEAD],
      key: SodiumKey[XChacha20AEAD],
      aad: AAD
  )(implicit S: ScalaSodium): Int =
    S.crypto_aead_xchacha20poly1305_ietf_decrypt(
      origOut,
      NullPtrInt,
      NullPtrBytes,
      ct.content,
      ct.content.length,
      aad,
      aad.length,
      ct.nonce,
      key
    )

  private[tsec] def sodiumEncryptDetachedAAD(
      cout: Array[Byte],
      tagOut: Array[Byte],
      pt: PlainText,
      nonce: Array[Byte],
      key: SodiumKey[XChacha20AEAD],
      aad: AAD
  )(implicit S: ScalaSodium): Int =
    S.crypto_aead_xchacha20poly1305_ietf_encrypt_detached(
      cout,
      tagOut,
      NullPtrInt,
      pt,
      pt.length,
      aad,
      aad.length,
      NullPtrBytes,
      nonce,
      key
    )

  private[tsec] def sodiumDecryptDetachedAAD(
      origOut: Array[Byte],
      ct: CipherText[XChacha20AEAD],
      tagIn: AuthTag[XChacha20AEAD],
      key: SodiumKey[XChacha20AEAD],
      aad: AAD
  )(implicit S: ScalaSodium): Int =
    S.crypto_aead_xchacha20poly1305_ietf_decrypt_detached(
      origOut,
      NullPtrBytes,
      ct.content,
      ct.content.length,
      tagIn,
      aad,
      aad.length,
      ct.nonce,
      key
    )

}
