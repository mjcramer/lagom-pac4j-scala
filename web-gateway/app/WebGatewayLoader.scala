import com.example.lps.api.UserService
import com.lightbend.lagom.scaladsl.api.{LagomConfigComponent, ServiceAcl, ServiceInfo}
import com.lightbend.lagom.scaladsl.client.LagomServiceClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext}
import com.lightbend.rp.servicediscovery.lagom.scaladsl.LagomServiceLocatorComponents
import com.softwaremill.macwire._
import controllers.MainController
import org.pac4j.play.scala.{DefaultSecurityComponents, SecurityComponents}
import org.pac4j.play.store.PlayCacheSessionStore
import org.webjars.play.WebJarsUtil
import play.api.inject.guice.{GuiceApplicationBuilder, GuiceApplicationLoader}
import controllers.AssetsComponents
import modules.SecurityModule
import play.api.ApplicationLoader.Context
import play.api.Mode.Prod
import play.api.inject.Injector
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, Configuration, Mode}
import play.controllers.AssetsComponents
import play.filters.HttpFiltersComponents
import play.filters.components.HttpFiltersComponents
import play.libs.ws.ahc.AhcWSComponents
import router.Routes

import scala.collection.immutable
import scala.concurrent.ExecutionContext

abstract class WebGateway(context: Context) extends GuiceApplicationLoader
  with AssetsComponents
  with HttpFiltersComponents
  with AhcWSComponents
  with LagomConfigComponent
  with LagomServiceClientComponents {






    override protected def builder(context: Context): GuiceApplicationBuilder = {
      val builder = initialBuilder.in(context.environment).overrides(overrides(context): _*)
      context.environment.mode match {
        case Prod => {
          // start mode
          val prodConf = Configuration(ConfigFactory.load("prod.conf"))
          builder.loadConfig(prodConf ++ context.initialConfiguration)
        }
        case Dev => {
          Logger.error("*** Custom Loader DEV****")
          // run mode
          val devConf = Configuration(ConfigFactory.load("dev.conf"))
          builder.loadConfig(devConf ++ context.initialConfiguration)
        }
        case Test => {
          Logger.error("*** Custom Loader TEST ****")
          // test mode
          val testConf = Configuration(ConfigFactory.load("test.conf"))
          builder.loadConfig(testConf ++ context.initialConfiguration)
        }
      }
    }




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

  import  play.api.inject.{bind => guiceBind}
  private val injector: Injector = GuiceApplicationBuilder(
    environment = environment,
    configuration = configuration)
        .bindings(new SecurityModule(environment, configuration))
        .bindings(guiceBind[SecurityComponents].to[DefaultSecurityComponents])
    .build().injector
  injector.instanceOf(classOf[SecurityComponents]) // use the injector to extract instances of the desired types from Guice

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
