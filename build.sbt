import build._

enablePlugins(BuildInfoPlugin)

baseSettings

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)

name := "qiitascala"

description := "Qiita Scala API client"

libraryDependencies ++= Seq(
  "io.argonaut" %% "argonaut-scalaz" % "6.2.3",
  "com.github.xuwei-k" %% "httpz" % httpzVersion,
  "com.github.xuwei-k" %% "httpz-native" % httpzVersion % "test",
  "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"
)
