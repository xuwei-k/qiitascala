package qiitascala

import argonaut.CodecJson
import httpz.JsonToString

final case class User(
  description       :Option[String],
  facebookId        :Option[String],
  followeesCount    :Long,
  followersCount    :Long,
  githubLoginName   :Option[String],
  id                :String,
  itemsCount        :Long,
  linkedinId        :Option[String],
  location          :Option[String],
  name              :String,
  organization      :Option[String],
  profileImageUrl   :String,
  twitterScreenName :Option[String],
  websiteUrl        :Option[String]
) extends JsonToString[User]

object User {

  implicit val userCodecJson: CodecJson[User] =
    CodecJson.casecodec14(apply, unapply)(
      "description",
      "facebook_id",
      "followees_count",
      "followers_count",
      "github_login_name",
      "id",
      "items_count",
      "linkedin_id",
      "location",
      "name",
      "organization",
      "profile_image_url",
      "twitter_screen_name",
      "website_url"
    )

}
