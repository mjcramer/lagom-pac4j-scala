
import sbt._
import scala.concurrent.duration._

// Top level package domain for this project
organization in ThisBuild := "com.example"

// Common settings for all subprojects
def commonSettings: Seq[Setting[_]] = Seq(
  scalaVersion in ThisBuild := Version.scala,
  evictionWarningOptions in update := EvictionWarningOptions.default.withWarnTransitiveEvictions(false),
  resolvers ++= Seq(Resolver.mavenLocal,
    "Scalaz Bintray" at "http://dl.bintray.com/scalaz/releases",
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    "Shibboleth Releases" at "https://build.shibboleth.net/nexus/content/repositories/releases/")
)

//======================================================================================================================
// LAGOM-PAC4J-SCALA (project root)
// ---------------------------------------------------------------------------------------------------------------------

lazy val `lagom-pac4j-scala` = (project in file("."))
  .settings(commonSettings: _*)
  .aggregate(security,
    `user-api`,`user-service`,
    `web-gateway`
  )
  .settings(
    name := "lagom-pac4j-scala"
  )


//======================================================================================================================
// Security Components
//----------------------------------------------------------------------------------------------------------------------
lazy val security = (project in file("security"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      lagomScaladslServer % Optional,
      //playJsonDerivedCodecs,
      Dependency.scalaTest
    )
  )

//======================================================================================================================
// Security Components
//----------------------------------------------------------------------------------------------------------------------
lazy val common = (project in file("common"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
//      lagomScaladslApi,
//      lagomScaladslServer % Optional,
      Dependency.playJsonDerivedCodecs,
      Dependency.scalaTest
    )
  )

lazy val `web-gateway` = (project in file("web-gateway"))
  .settings(commonSettings: _*)
  .enablePlugins(PlayScala && LagomPlay, SbtReactiveAppPlugin)
  .dependsOn(`user-api`)
  .settings(
    libraryDependencies ++= Seq(
      ehcache,
      Dependency.macwire,
      Dependency.scalaTest
    )
      ++ Library.webjars
      ++ Library.auth
  )

//======================================================================================================================
// USER SERVICE
//----------------------------------------------------------------------------------------------------------------------
lazy val `user-api` = (project in file("user-api"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      Dependency.playJsonDerivedCodecs
    )
  )
  .dependsOn(security)

lazy val `user-service` = (project in file("user-service"))
  .settings(commonSettings: _*)
  .enablePlugins(LagomScala) //, SbtReactiveAppPlugin)
  .dependsOn(
    `user-api`,
    common
  )
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslTestKit,
      Dependency.macwire,
      Dependency.scalaTest
//      Dependency.jwt
    )
  )
  .settings(lagomForkedTestSettings: _*)

// ------------------------------------------------------------------------------------------------
// External service configuration (for development environment)
// ------------------------------------------------------------------------------------------------
lagomCassandraCleanOnStart in ThisBuild := false
lagomCassandraMaxBootWaitingTime := (10 seconds)
//lagomUnmanagedServices in ThisBuild += (
//  "ldap" -> "http://127.0.0.1:9200",
//  "postgresql" -> "http://127.0.0.1:5432"
//)

//lagomCassandraMaxBootWaitingTime := 10


