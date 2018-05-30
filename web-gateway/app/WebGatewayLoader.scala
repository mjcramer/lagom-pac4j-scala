import com.example.lps.api.UserService
import com.lightbend.lagom.scaladsl.api.{ LagomConfigComponent, ServiceAcl, ServiceInfo }
import com.lightbend.lagom.scaladsl.client.LagomServiceClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.rp.servicediscovery.lagom.scaladsl.LagomServiceLocatorComponents
import com.softwaremill.macwire._
import controllers.{ AssetsComponents, MainController }
import org.pac4j.core.config.Config
import org.pac4j.play.scala.DefaultSecurityComponents
import org.pac4j.play.store.PlayCacheSessionStore
import org.webjars.play.WebJarsUtil
import play.api.ApplicationLoader.Context
import play.api.cache.DefaultSyncCacheApi
import play.api.cache.ehcache.EhCacheComponents
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.BodyParsers
import play.api.{ ApplicationLoader, BuiltInComponentsFromContext, Mode }
import play.cache.SyncCacheApiAdapter
import play.filters.HttpFiltersComponents
import router.Routes

import scala.collection.immutable
import scala.concurrent.ExecutionContext

abstract class WebGateway(context: Context) extends BuiltInComponentsFromContext(context)
  with AssetsComponents
  with HttpFiltersComponents
  with AhcWSComponents
  with LagomConfigComponent
  with LagomServiceClientComponents
  with EhCacheComponents
{

  override lazy val serviceInfo: ServiceInfo = ServiceInfo(
    "web-gateway",
    Map(
      "web-gateway" -> immutable.Seq(ServiceAcl.forPathRegex("(?!/api/).*"))
    )
  )
  override implicit lazy val executionContext: ExecutionContext = actorSystem.dispatcher


  lazy val errHandler  = httpErrorHandler
  override lazy val router = {
    val prefix = "/"
    wire[Routes]
  }

  //  private val injector: Injector = GuiceApplicationBuilder(
  //    environment = environment,
  //    configuration = configuration)
  //        .bindings(new SecurityModule(environment, configuration))
  //        .bindings(guiceBind[SecurityComponents].to[DefaultSecurityComponents])
  //    .build().injector
  //  injector.instanceOf(classOf[SecurityComponents]) // use the injector to extract instances of the desired types from Guice

  lazy val pacSyncCache:play.api.cache.SyncCacheApi = new DefaultSyncCacheApi(defaultCacheApi)
  lazy val pcSyncCache:play.cache.SyncCacheApi = wire[SyncCacheApiAdapter]
  lazy val sessionStore: org.pac4j.play.store.PlaySessionStore = wire[PlayCacheSessionStore]

  lazy val pac4jConfig: org.pac4j.core.config.Config = new Config()
  lazy val secComponents: org.pac4j.play.scala.SecurityComponents = wire[DefaultSecurityComponents]

  lazy val bodyParsersDefault = wire[BodyParsers.Default]

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
