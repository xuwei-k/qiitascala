package qiitascala

import argonaut.CodecJson
import httpz.JsonToString

final case class Comment(
  body :String,
  id   :String,
  user :User
) extends JsonToString[Comment]

object Comment {

  implicit val commentCodecJson: CodecJson[Comment] =
    CodecJson.casecodec3(apply, unapply)(
      "body",
      "id",
      "user"
    )

}
