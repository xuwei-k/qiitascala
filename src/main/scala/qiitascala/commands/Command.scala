package qiitascala
package commands

import argonaut.{DecodeJson, Json}
import httpz._
import scalaz.Free.FreeC
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

  final def lift[F[_]](implicit I: Inject[Command, F]): FreeC[F, A] =
    Free.liftFC(I.inj(this))

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

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/comments/:id]] */
final case class GetComment(id: String) extends Command[Json](
  Command.get(s"comments/$id")
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/items/:item_id/comments]] */
final case class GetComments(itemId: String, page: Int) extends Command[IList[Json]](
  Command.get(s"items/$itemId/comments", Request.param("page", page.toString))
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/tags]] */
final case class GetTags(page: Int) extends Command[IList[Json]](
  Command.get(s"tags", Request.param("page", page.toString))
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/tags/:id]] */
final case class GetTag(name: String) extends Command[Json](
  Command.get(s"tags/$name")
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/users/:user_id/following_tags]] */
final case class GetFollowingTags(userId: String, page: Int) extends Command[IList[Json]](
  Command.get(s"users/$userId/following_tags", Request.param("page", page.toString))
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/users]] */
final case class GetUsers(page: Int) extends Command[IList[Json]](
  Command.get(s"users", Request.param("page", page.toString))
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/users/:id]] */
final case class GetUser(userId: String) extends Command[Json](
  Command.get(s"users/$userId")
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/authenticated_user]] */
case object GetAuthenticatedUser extends Command[Json](
  Command.get(s"authenticated_user")
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/users/:user_id/followees]] */
final case class GetFollowees(userId: String, page: Int) extends Command[Json](
  Command.get(s"users/$userId/followees", Request.param("page", page.toString))
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/users/:user_id/followers]] */
final case class GetFollowers(userId: String, page: Int) extends Command[Json](
  Command.get(s"users/$userId/followers", Request.param("page", page.toString))
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/items/:item_id/stockers]] */
final case class GetStockers(itemId: String, page: Int) extends Command[Json](
  Command.get(s"items/$itemId/stockers", Request.param("page", page.toString))
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/items]] */
final case class GetItems(page: Int) extends Command[IList[Json]](
  Command.get(s"items", Request.param("page", page.toString))
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/items/:id]] */
final case class GetItem(itemId: String) extends Command[Json](
  Command.get(s"items/$itemId")
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/tags/:id/items]] */
final case class GetTaggedItems(tag: String, page: Int) extends Command[IList[Json]](
  Command.get(s"tags/$tag/items", Request.param("page", page.toString))
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/users/:user_id/items]] */
final case class GetUserItems(userId: String, page: Int) extends Command[IList[Json]](
  Command.get(s"users/$userId/items", Request.param("page", page.toString))
)

/** @see [[http://qiita.com/api/v2/docs#get-/api/v2/users/:user_id/stocks]] */
final case class GetUserStocks(userId: String, page: Int) extends Command[IList[Json]](
  Command.get(s"users/$userId/stocks", Request.param("page", page.toString))
)
