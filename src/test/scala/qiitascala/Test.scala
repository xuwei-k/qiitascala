package qiitascala

import argonaut.{EncodeJson, PrettyParams}
import argonaut.ArgonautScalaz._
import httpz._
import httpz.native._
import org.scalacheck.{Prop, Properties}
import qiitascala.commands._
import scalaz.\/

abstract class QiitaSpec[A](val command: Command[A])(implicit A: EncodeJson[A]) extends Properties(command.getClass.toString) {

  val action = Qiita.commands2action(Qiita.lift(command))

  def run(): Error \/ A = action.interpret

  property(name) = Prop.secure{
    val result = run()
    result.foreach{ j =>
      //println(A(j).pretty(PrettyParams.spaces2))
    }
    result.leftMap(_.fold(
      println,
      (res, error) => println((res, error)),
      (_, error, history, _) => println((error, history)))
    )
    result.isRight
  }
}

object GetCommentSpec extends QiitaSpec(GetComment("77e21d1799c5348dd52c"))

object GetCommentsSpec extends QiitaSpec(GetComments("5df3ad7e53216df5f65f", 1))

object GetTagsSpec extends QiitaSpec(GetTags(1))

object GetTagSpec extends QiitaSpec(GetTag("scala"))

object GetFollowingTagsSpec extends QiitaSpec(GetFollowingTags("xuwei_k", 1))

object GetUsersSpec extends QiitaSpec(GetUsers(1))

object GetUserSpec extends QiitaSpec(GetUser("xuwei_k"))

object GetFolloweesSpec extends QiitaSpec(GetFollowees("xuwei_k", 1))

object GetFollowersSpec extends QiitaSpec(GetFollowers("xuwei_k", 1))

object GetStockersSpec extends QiitaSpec(GetStockers("5df3ad7e53216df5f65f", 1))

object GetItemsSpec extends QiitaSpec(GetItems(1))

object GetItemSpec extends QiitaSpec(GetItem("5df3ad7e53216df5f65f"))

object GetTaggedItemsSpec extends QiitaSpec(GetTaggedItems("scala", 1))

object GetUserItemsSpec extends QiitaSpec(GetUserItems("mzp", 1))

object GetUserStocksSpec extends QiitaSpec(GetUserStocks("tototoshi", 1))
