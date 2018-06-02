package components

import com.softwaremill.macwire._
import controllers._
import org.pac4j.core.profile.CommonProfile
import org.pac4j.play.scala.{Pac4jScalaTemplateHelper, SecurityComponents}

trait ControllersComponents {

  implicit def securityComponents: SecurityComponents
  implicit def scalaTemplateHelper: Pac4jScalaTemplateHelper[CommonProfile]

  lazy val applicationController = wire[MainController]
  lazy val applicationWithFilter = wire[MainControllerWithFilter]
  lazy val applicationWithScalaHelper = wire[MainControllerWithScalaHelper]
  lazy val centralLogoutController = wire[CentralLogoutController]
}
