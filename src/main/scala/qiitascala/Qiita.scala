package qiitascala

import httpz.Action
import qiitascala.commands.Command
import scalaz.{Inject, Free}

object Qiita extends Qiita[Command] {
  implicit def instance[F[_]](implicit I: Inject[Command, F]): Qiita[F] =
    new Qiita[F]

  def commands2action[A](a: Commands[A]): Action[A] =
    a.foldMap(interpreter)(httpz.ActionMonad)

  private[qiitascala] final val baseURL = "https://qiita.com/api/v2/"
}


class Qiita[F[_]](implicit I: Inject[Command, F]) {

  private[this] type FreeF[A] = Free[F, A]

  final def lift[A](f: Command[A]): FreeF[A] =
    Free.liftF(I.inj(f))
}
