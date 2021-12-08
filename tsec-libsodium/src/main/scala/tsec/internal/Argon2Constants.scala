package tsec.internal

private[tsec] trait Argon2Constants {

  val crypto_pwhash_argon2id_ALG_ARGON2ID13       = 2
  val crypto_pwhash_argon2id_BYTES_MIN            = 16L
  val crypto_pwhash_argon2id_BYTES_MAX            = 4294967295L
  val crypto_pwhash_argon2id_PASSWD_MIN           = 0L
  val crypto_pwhash_argon2id_PASSWD_MAX           = 4294967295L
  val crypto_pwhash_argon2id_SALTBYTES            = 16
  val crypto_pwhash_argon2id_STRBYTES             = 128
  val crypto_pwhash_argon2id_OPSLIMIT_MIN         = 1
  val crypto_pwhash_argon2id_OPSLIMIT_MAX         = 4294967295L
  val crypto_pwhash_argon2id_MEMLIMIT_MIN         = 8192
  val crypto_pwhash_argon2id_MEMLIMIT_MAX         = 4398046510080L
  val crypto_pwhash_argon2id_OPSLIMIT_INTERACTIVE = 2
  val crypto_pwhash_argon2id_MEMLIMIT_INTERACTIVE = 67108864
  val crypto_pwhash_argon2id_OPSLIMIT_MODERATE    = 3
  val crypto_pwhash_argon2id_MEMLIMIT_MODERATE    = 268435456
  val crypto_pwhash_argon2id_OPSLIMIT_SENSITIVE   = 4
  val crypto_pwhash_argon2id_MEMLIMIT_SENSITIVE   = 1073741824

  def crypto_pwhash_ALG_DEFAULT        = crypto_pwhash_argon2id_ALG_ARGON2ID13
  def crypto_pwhash_SALTBYTES          = crypto_pwhash_argon2id_SALTBYTES
  def crypto_pwhash_STRBYTES           = crypto_pwhash_argon2id_STRBYTES
  def crypto_pwhash_BYTES_MIN          = crypto_pwhash_argon2id_BYTES_MIN
  def crypto_pwhash_OPSLIMIT_SENSITIVE = crypto_pwhash_argon2id_OPSLIMIT_SENSITIVE
  def crypto_pwhash_MEMLIMIT_SENSITIVE = crypto_pwhash_argon2id_MEMLIMIT_SENSITIVE

}
