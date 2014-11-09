package qiitascala

import httpz.Action
import qiitascala.commands.Command
import scalaz.Free.FreeC
import scalaz.{Inject, Free}

object Qiita extends Qiita[Command] {
  implicit def instance[F[_]](implicit I: Inject[Command, F]): Qiita[F] =
    new Qiita[F]

  def commands2action[A](a: Commands[A]): Action[A] =
    Free.runFC[Command, Action, A](a)(interpreter)(httpz.ActionMonad)

  private[qiitascala] final val baseURL = "https://qiita.com/api/v2/"
}


class Qiita[F[_]](implicit I: Inject[Command, F]) {

  private[this] type FreeCF[A] = FreeC[F, A]

  final def lift[A](f: Command[A]): FreeCF[A] =
    Free.liftFC(I.inj(f))
}
