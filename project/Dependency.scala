
import sbt._

object Version {
  val akka = "2.5.12"
  val macwire = "2.3.1"
  val slick = "3.0.3"
  val scala = "2.12.6"
  val scalaTest = "3.0.1"
  val pac4j = "3.0.0-RC3-SNAPSHOT"
  val play = "2.6.15"
  val playMailer = "6.0.1"
  val playSlick = "0.8.1"
  val playAuth = "0.13.0"
  val playJsonDerivedCodecs = "4.0.0"
  val playPac4j = "6.0.0-RC3-SNAPSHOT"
}

object Dependency {
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % Version.akka
  val akkaCluster = "com.typesafe.akka" %% "akka-cluster" % Version.akka
  val akkaContrib = "com.typesafe.akka" %% "akka-contrib" % Version.akka
  val akkaRemote = "com.typesafe.akka" %% "akka-remote" %  Version.akka
//  val jwt = "com.pauldijou" %% "jwt-play-json" % "0.12.1"
  val macwire = "com.softwaremill.macwire" %% "macros" % Version.macwire % "provided"
  val pac4jLdap = "org.pac4j" % "pac4j-ldap" % Version.pac4j
  val pac4jHttp = "org.pac4j" % "pac4j-http" % Version.pac4j
  val pac4jJwt = "org.pac4j" % "pac4j-jwt" % Version.pac4j
  val pac4jSql = "org.pac4j" % "pac4j-sql" % Version.pac4j
  val pac4jKerberos = "org.pac4j" % "pac4j-kerberos" % Version.pac4j
  val pac4jCas = "org.pac4j" % "pac4j-cas" % Version.pac4j
  val pac4jOpenId = "org.pac4j" % "pac4j-openid" % Version.pac4j exclude("xml-apis" , "xml-apis")
  val pac4jOAuth = "org.pac4j" % "pac4j-oauth" % Version.pac4j
  val pac4jSaml = "org.pac4j" % "pac4j-saml" % Version.pac4j
  val pac4jOidc = "org.pac4j" % "pac4j-oidc" % Version.pac4j exclude("commons-io" , "commons-io")
  val pac4jGae = "org.pac4j" % "pac4j-gae" % Version.pac4j
  val pac4jMongo = "org.pac4j" % "pac4j-mongo" % Version.pac4j
  val pac4jCouch = "org.pac4j" % "pac4j-couch" % Version.pac4j
  val playAuth = "jp.t2v" %% "play2-auth" % Version.playAuth
  val playAuthTest = "jp.t2v" %% "play2-auth-test" % Version.playAuth % "test"
  val playCache = "com.typesafe.play" %% "play-cache" % Version.play
  val playPac4j = "org.pac4j" %% "play-pac4j" % Version.playPac4j
  val playSlick = "com.typesafe.play" %% "play-slick" % Version.playSlick
  val playJsonDerivedCodecs = "org.julienrf" %% "play-json-derived-codecs" % Version.playJsonDerivedCodecs
  val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest % "test"
  val slick = "com.typesafe.slick" %% "slick" % Version.slick
  val slickCodegen = "com.typesafe.slick" %% "slick-codegen" % Version.slick
  val slickExtensions = "com.typesafe.slick" %% "slick-extensions" % Version.slick
//  val base64 = "me.lessis" %% "base64" % "0.2.0"
//  val accord = "com.wix" %% "accord-core" % "0.6.1"
  val webjarsPlay = "org.webjars" %% "webjars-play" % "2.6.3"
  val webjarsJquery = "org.webjars" % "jquery" % "3.3.1"
  val webjarsPopper =  "org.webjars" % "popper.js" % "1.14.1"
  val webjarsBootstrap = "org.webjars" % "bootstrap" % "4.1.0"
}

object Library {
  import Dependency._
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
