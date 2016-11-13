import build._

enablePlugins(BuildInfoPlugin)

baseSettings

name := "qiitascala"

description := "Qiita Scala API client"

libraryDependencies ++= Seq(
  "io.argonaut" %% "argonaut-scalaz" % "6.2-RC1",
  "com.github.xuwei-k" %% "httpz" % httpzVersion,
  "com.github.xuwei-k" %% "httpz-native" % httpzVersion % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)
