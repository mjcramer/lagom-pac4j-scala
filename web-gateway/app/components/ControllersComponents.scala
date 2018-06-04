package components

import com.softwaremill.macwire._
import controllers._
import org.pac4j.core.profile.CommonProfile
import org.pac4j.play.scala.{Pac4jScalaTemplateHelper, SecurityComponents}
import org.webjars.play.WebJarsUtil

import scala.concurrent.ExecutionContext

trait ControllersComponents {

  implicit def securityComponents: SecurityComponents
  implicit def scalaTemplateHelper: Pac4jScalaTemplateHelper[CommonProfile]
  implicit val executionContext: ExecutionContext
  implicit val webjars: WebJarsUtil

  lazy val applicationController = wire[MainController]
  lazy val applicationWithFilter = wire[MainControllerWithFilter]
  lazy val applicationWithScalaHelper = wire[MainControllerWithScalaHelper]
  lazy val centralLogoutController = wire[CentralLogoutController]
}
