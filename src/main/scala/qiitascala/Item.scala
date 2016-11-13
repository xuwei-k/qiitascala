package qiitascala

import argonaut.ArgonautScalaz._
import argonaut.CodecJson
import httpz.JsonToString
import scalaz.IList

final case class Item(
  body      :String,
  coediting :Boolean,
  createdAt :String,
  id        :String,
  isPrivate :Boolean,
  tags      :IList[Item.Tag],
  title     :String,
  updatedAt :String,
  user      :User
) extends JsonToString[Item]

object Item {

  implicit val itemCodecJson: CodecJson[Item] =
    CodecJson.casecodec9(apply, unapply)(
      "body",
      "coediting",
      "created_at",
      "id",
      "private",
      "tags",
      "title",
      "updated_at",
      "user"
    )

  final case class Tag(
    iconUrl        :Option[String],
    name           :String,
    versions       :IList[String]
  ) extends JsonToString[Item.Tag]

  object Tag {

    implicit val tagCodecJson: CodecJson[Tag] =
      CodecJson.casecodec3(apply, unapply)(
        "icon_url",
        "name",
        "versions"
      )

  }
}
