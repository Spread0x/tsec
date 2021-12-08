package tsec.cipher.symmetric.jca

import cats.Applicative
import cats.effect.Sync
import tsec.cipher.symmetric._
import tsec.common.ManagedRandom

object JCAIvGen {
  def random[F[_], A](implicit C: BlockCipher[A], F: Sync[F]): IvGen[F, A] =
    new IvGen[F, A] with ManagedRandom {

      def genIv: F[Iv[A]] =
        F.delay(genIvUnsafe)

      def genIvUnsafe: Iv[A] = {
        val nonce = new Array[Byte](C.blockSizeBytes)
        nextBytes(nonce)
        Iv[A](nonce)
      }
    }

  def emptyIv[F[_], A](implicit F: Applicative[F]): IvGen[F, A] =
    new IvGen[F, A] {
      protected val cachedEmpty = Array.empty[Byte]

      def genIv: F[Iv[A]] =
        F.pure(Iv[A](cachedEmpty))

      def genIvUnsafe: Iv[A] = Iv[A](cachedEmpty)
    }
}

trait CounterIvGen[F[_], A] extends IvGen[F, A] {
  def refresh: F[Unit]

  def counterState: F[Long]

  def unsafeCounterState: Long
}
