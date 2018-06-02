import com.example.lps.api.UserService
import com.lightbend.lagom.scaladsl.api.{LagomConfigComponent, ServiceAcl, ServiceInfo}
import com.lightbend.lagom.scaladsl.client.LagomServiceClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.rp.servicediscovery.lagom.scaladsl.LagomServiceLocatorComponents
import com.softwaremill.macwire._
import controllers.{MainController, AssetsComponents, CustomErrorHandler}
import components.{ControllersComponents, FiltersComponents, Pac4JComponents}
import org.pac4j.core.config.Config
import org.pac4j.play.scala.{DefaultSecurityComponents, SecurityComponents}
import org.pac4j.play.store.PlayCacheSessionStore
import org.webjars.play.WebJarsUtil
import play.api.ApplicationLoader.Context
import play.api.cache.DefaultSyncCacheApi
import play.api.cache.ehcache.EhCacheComponents
import play.api.http.HttpErrorHandler
import play.api.i18n.I18nComponents
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.BodyParsers
import play.api.routing.Router
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator, Mode}
import play.cache.SyncCacheApiAdapter
import play.filters.HttpFiltersComponents
import router.Routes

import scala.collection.immutable
import scala.concurrent.ExecutionContext


abstract class WebGateway(context: Context) extends BuiltInComponentsFromContext(context)
  with Pac4JComponents
  with ControllersComponents
  with FiltersComponents
  with AssetsComponents
  with I18nComponents
  with AhcWSComponents
  with LagomConfigComponent
  with LagomServiceClientComponents
  with EhCacheComponents
{
  // set up logger
  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }

  // This is needed because Pac4J DefaultSecurityComponents injects a BodyParser.Default
  lazy val wiredDefaultBodyParser:BodyParsers.Default = new BodyParsers.Default(playBodyParsers)

  lazy val securityComponents: SecurityComponents = DefaultSecurityComponents(
    playSessionStore, pac4JConfig, wiredDefaultBodyParser, controllerComponents
  )

  override lazy val httpErrorHandler: HttpErrorHandler = wire[CustomErrorHandler]

  override lazy val router: Router = {
    val prefix: String = "/"
    wire[Routes]
  }

  override lazy val serviceInfo: ServiceInfo = ServiceInfo(
    "web-gateway",
    Map(
      "web-gateway" -> immutable.Seq(ServiceAcl.forPathRegex("(?!/api/).*"))
    )
  )

  override implicit lazy val executionContext: ExecutionContext = actorSystem.dispatcher

  lazy implicit val webjars = wire[WebJarsUtil]
  lazy val main = wire[MainController]
  lazy val userService = serviceClient.implement[UserService]
}


class WebGatewayLoader extends ApplicationLoader {
  override def load(context: Context) = context.environment.mode match {
    case Mode.Dev =>
      (new WebGateway(context) with LagomDevModeComponents).application
    case _ =>
      (new WebGateway(context) with LagomServiceLocatorComponents).application
  }
}
