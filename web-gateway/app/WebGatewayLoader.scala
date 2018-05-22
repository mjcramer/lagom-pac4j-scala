import com.example.lps.api.UserService
import com.lightbend.lagom.scaladsl.api.{LagomConfigComponent, ServiceAcl, ServiceInfo}
import com.lightbend.lagom.scaladsl.client.LagomServiceClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.softwaremill.macwire._
import controllers.MainController
import modules.SecurityModule
import org.pac4j.play.scala.{DefaultSecurityComponents, SecurityComponents}
import org.pac4j.play.store.PlayCacheSessionStore
import org.webjars.play.WebJarsUtil
import play.cache.{AsyncCacheApi, SyncCacheApi}
import play.mvc.BodyParser
//import com.lightbend.rp.servicediscovery.lagom.scaladsl.LagomServiceLocatorComponents
import controllers.{Assets, AssetsComponents}
import play.api.ApplicationLoader.Context
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.EssentialFilter
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, Mode}
import play.filters.HttpFiltersComponents
import play.i18n.I18nComponents
import router.Routes

import scala.collection.immutable
import scala.concurrent.ExecutionContext

abstract class WebGateway(context: Context) extends BuiltInComponentsFromContext(context)
  with AssetsComponents
  with HttpFiltersComponents
  with AhcWSComponents
  with LagomConfigComponent
  with LagomServiceClientComponents
  with SecurityComponents {

  override lazy val serviceInfo: ServiceInfo = ServiceInfo(
    "web-gateway",
    Map(
      "web-gateway" -> immutable.Seq(ServiceAcl.forPathRegex("(?!/api/).*"))
    )
  )
  override implicit lazy val executionContext: ExecutionContext = actorSystem.dispatcher

  override lazy val router = {
    val prefix = "/"
    wire[Routes]
  }

//  lazy val security = wire[SecurityModule]
//  lazy val userService = serviceClient.implement[UserService]
//
  lazy implicit val webjars = wire[WebJarsUtil]

  lazy val security = wire[SecurityModule]
  lazy val main = wire[MainController]

  override val controllerComponents = wire[DefaultSecurityComponents]
  override val components = wire[DefaultSecurityComponents]

//  lazy val syncCache = wire[SyncCacheApi]
//  lazy val asyncCache = wire[AsyncCacheApi]
//  lazy val playSessionStore = wire[PlayCacheSessionStore]

  override def parser = ???
}

class WebGatewayLoader extends ApplicationLoader {
  override def load(context: Context) = context.environment.mode match {
    case _ =>
    //    case Mode.Dev =>
      (new WebGateway(context) with LagomDevModeComponents).application
//    case _ =>
//      (new WebGateway(context) with LagomServiceLocatorComponents).application
  }
}
