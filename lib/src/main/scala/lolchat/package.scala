import java.util.concurrent.Executors

import cats.data._
import cats.free.Free
import lolchat.data.ExeCtx
import lolchat.free.ChatF
import lolchat.model.Session

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

package object lolchat extends AnyRef with ops {
  type Chat[A] = Free[ChatF, A]
  type ChatOp[A] = ReaderT[Chat, Session, A]
  type ErrMsg = String

  private[lolchat] implicit val exeCtx = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(8))
  implicit def futureInstance(implicit exeCtx: ExeCtx) = cats.implicits.futureInstance

  val LoLChat = free.interp.SmackXmppInterp

}