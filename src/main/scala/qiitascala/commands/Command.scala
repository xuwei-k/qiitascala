package qiitascala
package commands

import argonaut.{DecodeJson, Json}
import httpz._
import scalaz.{Free, IList, Inject, NonEmptyList}

sealed abstract class Command[A](val f: String => Request)(implicit val decoder: DecodeJson[A]) extends Product with Serializable{
  final def request: httpz.Request =
    requestWithURL(Qiita.baseURL)

  final def requestWithURL(baseURL: String): httpz.Request =
    f(baseURL)

  final def actionWithURL(baseURL: String): httpz.Action[A] =
    Core.json[A](requestWithURL(baseURL))(decoder)

  final def action: httpz.Action[A] =
    actionWithURL(Qiita.baseURL)

  final def lift[F[_]](implicit I: Inject[Command, F]): Free[F, A] =
    Free.liftF(I.inj(this))

  final def actionEOps: ActionEOps[httpz.Error, A] =
    new ActionEOps(action)

  final def nel: ActionE[NonEmptyList[httpz.Error], A] =
    actionEOps.nel
}

object Command {
  private[qiitascala] def get(url: String, opt: Config = httpz.emptyConfig): String => Request = {
    baseURL => opt(Request(url = baseURL + url, params = Map("per_page" -> "100")))
  }
}

final case class GetComment(id: String) extends Command[Comment](
  Command.get(s"comments/$id")
)

final case class GetComments(itemId: String, page: Int) extends Command[IList[Comment]](
  Command.get(s"items/$itemId/comments", Request.param("page", page.toString))
)

final case class GetTags(page: Int) extends Command[IList[Tag]](
  Command.get(s"tags", Request.param("page", page.toString))
)

final case class GetTag(name: String) extends Command[Tag](
  Command.get(s"tags/$name")
)

final case class GetFollowingTags(userId: String, page: Int) extends Command[IList[Json]](
  Command.get(s"users/$userId/following_tags", Request.param("page", page.toString))
)

final case class GetUsers(page: Int) extends Command[IList[User]](
  Command.get(s"users", Request.param("page", page.toString))
)

final case class GetUser(userId: String) extends Command[User](
  Command.get(s"users/$userId")
)

case object GetAuthenticatedUser extends Command[User](
  Command.get(s"authenticated_user")
)

final case class GetFollowees(userId: String, page: Int) extends Command[IList[User]](
  Command.get(s"users/$userId/followees", Request.param("page", page.toString))
)

final case class GetFollowers(userId: String, page: Int) extends Command[IList[User]](
  Command.get(s"users/$userId/followers", Request.param("page", page.toString))
)

final case class GetStockers(itemId: String, page: Int) extends Command[IList[User]](
  Command.get(s"items/$itemId/stockers", Request.param("page", page.toString))
)

final case class GetItems(page: Int) extends Command[IList[Item]](
  Command.get(s"items", Request.param("page", page.toString))
)

final case class GetItem(itemId: String) extends Command[Item](
  Command.get(s"items/$itemId")
)

final case class GetTaggedItems(tag: String, page: Int) extends Command[IList[Item]](
  Command.get(s"tags/$tag/items", Request.param("page", page.toString))
)

final case class GetUserItems(userId: String, page: Int) extends Command[IList[Item]](
  Command.get(s"users/$userId/items", Request.param("page", page.toString))
)

final case class GetUserStocks(userId: String, page: Int) extends Command[IList[Item]](
  Command.get(s"users/$userId/stocks", Request.param("page", page.toString))
)
