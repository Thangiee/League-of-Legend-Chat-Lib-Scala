import java.util.concurrent.Executors

import cats.Functor
import cats.data._
import cats.free.Free
import lolchat.data.ExeCtx
import lolchat.free.ChatF
import lolchat.model.Session
import rx.{Ctx, Rx}

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

package object lolchat extends AnyRef with ops {
  type Chat[A] = Free[ChatF, A]
  type ChatOp[A] = ReaderT[Chat, Session, A]
  type ErrMsg = String

  private[lolchat] implicit val exeCtx = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(8))
  implicit def futureInstance(implicit exeCtx: ExeCtx) = cats.implicits.futureInstance

  implicit class RxOptionOp[T](val rx: OptionT[Rx, T]) extends AnyVal {
    def kill(): Unit = rx.value.kill()
  }

  val LoLChat = free.interp.SmackXmppInterp

  implicit def rxFunctorInst(implicit ctx: Ctx.Owner) = new Functor[Rx] {
    def map[A, B](fa: Rx[A])(f: (A) => B): Rx[B] = fa.map(f)
  }
}
