package qiitascala

import qiitascala.commands.Command

private[qiitascala] object Interpreter extends CommandToAction {
  override def apply[A](fa: Command[A]) = fa match {
    case c => c.action
  }
}
