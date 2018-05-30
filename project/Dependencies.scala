
import sbt._

object Versions {
  val akka = "2.5.9"
  val jtds = "1.3.1"
  val macwire = "2.2.5"
  val slick = "3.0.3"
  val scala = "2.11.11"
  val scalaTest = "3.0.1"
  val pac4j = "3.0.0-RC1"
  val playMailer = "6.0.1"
  val playSlick = "0.8.1"
  val playAuth = "0.13.0"
  val playJsonDerivedCodecs = "4.0.0"
  val playPac4j = "6.0.0-RC1"
}

object Dependencies {
  val playMailer = "com.typesafe.play" %% "play-mailer" % Versions.playMailer
  val playMailerGuice = "com.typesafe.play" %% "play-mailer-guice" % Versions.playMailer
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % Versions.akka
  val akkaCluster = "com.typesafe.akka" %% "akka-cluster" % Versions.akka
  val akkaContrib = "com.typesafe.akka" %% "akka-contrib" % Versions.akka
  val akkaRemote = "com.typesafe.akka" %% "akka-remote" %  Versions.akka
  val jtds = "net.sourceforge.jtds" % "jtds" % Versions.jtds
  val macwire = "com.softwaremill.macwire" %% "macros" % Versions.macwire % "provided"
  val playPac4j = "org.pac4j" %% "play-pac4j" % Versions.playPac4j
  val pac4jLdap = "org.pac4j" % "pac4j-ldap" % Versions.pac4j
  val pac4jHttp = "org.pac4j" % "pac4j-http" % Versions.pac4j
  val pac4jJwt = "org.pac4j" % "pac4j-jwt" % Versions.pac4j
  val pac4jSql = "org.pac4j" % "pac4j-sql" % Versions.pac4j
  val pac4jKerberos = "org.pac4j" % "pac4j-kerberos" % Versions.pac4j
  val pac4jCas = "org.pac4j" % "pac4j-cas" % Versions.pac4j
  val pac4jOpenId = "org.pac4j" % "pac4j-openid" % Versions.pac4j exclude("xml-apis" , "xml-apis")
  val pac4jOAuth = "org.pac4j" % "pac4j-oauth" % Versions.pac4j
  val pac4jSaml = "org.pac4j" % "pac4j-saml" % Versions.pac4j
  val pac4jOidc = "org.pac4j" % "pac4j-oidc" % Versions.pac4j exclude("commons-io" , "commons-io")
  val pac4jGae = "org.pac4j" % "pac4j-gae" % Versions.pac4j
  val pac4jMongo = "org.pac4j" % "pac4j-mongo" % Versions.pac4j
  val pac4jCouch = "org.pac4j" % "pac4j-couch" % Versions.pac4j
  val playSlick = "com.typesafe.play" %% "play-slick" % Versions.playSlick
  val playAuth = "jp.t2v" %% "play2-auth" % Versions.playAuth
  val playAuthTest = "jp.t2v" %% "play2-auth-test" % Versions.playAuth % "test"
  val playJsonDerivedCodecs = "org.julienrf" %% "play-json-derived-codecs" % Versions.playJsonDerivedCodecs
  val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % "test"
  val slick = "com.typesafe.slick" %% "slick" % Versions.slick
  val slickCodegen = "com.typesafe.slick" %% "slick-codegen" % Versions.slick
  val slickExtensions = "com.typesafe.slick" %% "slick-extensions" % Versions.slick
  val base64 = "me.lessis" %% "base64" % "0.2.0"
  val jwt = "com.pauldijou" %% "jwt-play-json" % "0.12.1"
  val accord = "com.wix" %% "accord-core" % "0.6.1"
  val webjarsPlay = "org.webjars" %% "webjars-play" % "2.6.3"
  val webjarsJquery = "org.webjars" % "jquery" % "3.3.1"
  val webjarsPopper =  "org.webjars" % "popper.js" % "1.14.1"
  val webjarsBootstrap = "org.webjars" % "bootstrap" % "4.1.0"
}

object Libraries {
  import Dependencies._
  val auth = Seq(
    playPac4j,
    pac4jLdap,
    pac4jHttp,
    pac4jSql,
    pac4jKerberos,
    pac4jJwt,
    pac4jOAuth,
    pac4jSaml,
    pac4jCas,
    pac4jOidc
  )
  val webjars = Seq(
    webjarsBootstrap,
    webjarsJquery,
    webjarsPopper,
    webjarsPlay
  )
}
