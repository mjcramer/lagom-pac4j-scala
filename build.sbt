
import sbt._
import scala.concurrent.duration._

// Top level package domain for this project
organization in ThisBuild := "com.example"

// The Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

// ------------------------------------------------------------------------------------------------
// External service configuration (for development environment)
// ------------------------------------------------------------------------------------------------
lagomCassandraCleanOnStart in ThisBuild := false
lagomCassandraMaxBootWaitingTime := (10 seconds)
lagomUnmanagedServices in ThisBuild += ("ldap" -> "http://127.0.0.1:9200")

//lagomCassandraMaxBootWaitingTime := 10


// Common settings for all subprojects
def commonSettings: Seq[Setting[_]] = Seq(
  evictionWarningOptions in update := EvictionWarningOptions.default.withWarnTransitiveEvictions(false)
)

//======================================================================================================================
// PARS (project root)
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
      Dependencies.scalaTest
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
      Dependencies.playJsonDerivedCodecs,
      Dependencies.scalaTest
    )
  )

lazy val `web-gateway` = (project in file("web-gateway"))
  .settings(commonSettings: _*)
  .enablePlugins(PlayScala && LagomPlay, SbtReactiveAppPlugin)
  .dependsOn(`user-api`)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslServer,
      ehcache,
      cacheApi,
      guice,
      Dependencies.macwire,
      Dependencies.scalaTest
    )
      ++ Libraries.webjars
      ++ Libraries.auth
  )

//======================================================================================================================
// USER SERVICE
//----------------------------------------------------------------------------------------------------------------------
lazy val `user-api` = (project in file("user-api"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi,
      Dependencies.playJsonDerivedCodecs
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
      Dependencies.macwire,
      Dependencies.scalaTest,
      Dependencies.jwt
    )
  )
  .settings(lagomForkedTestSettings: _*)
