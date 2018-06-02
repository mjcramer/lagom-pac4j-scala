package loader

import com.softwaremill.macwire._
import controllers.{Application, ApplicationWithFilter, ApplicationWithScalaHelper, CentralLogoutController}
import org.pac4j.core.profile.CommonProfile
import org.pac4j.play.scala.{Pac4jScalaTemplateHelper, SecurityComponents}

trait ControllersComponents {

  implicit def securityComponents: SecurityComponents
  implicit def scalaTemplateHelper: Pac4jScalaTemplateHelper[CommonProfile]

  lazy val applicationController: Application = wire[Application]
  lazy val applicationWithFilter: ApplicationWithFilter = wire[ApplicationWithFilter]
  lazy val applicationWithScalaHelper: ApplicationWithScalaHelper = wire[ApplicationWithScalaHelper]
  lazy val centralLogoutController: CentralLogoutController = wire[CentralLogoutController]
}
