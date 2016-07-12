import httpz.Action
import qiitascala.commands.Command
import scalaz.{Free, ~>}

package object qiitascala{

  type CommandToAction = Command ~> Action

  type Commands[A] = Free[Command, A]

  val interpreter: CommandToAction = Interpreter
}
