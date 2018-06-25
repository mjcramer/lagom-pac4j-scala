package controllers

import org.pac4j.core.config.Config
import org.pac4j.core.engine.CallbackLogic
import org.pac4j.core.engine.DefaultCallbackLogic
import org.pac4j.play.store.PlaySessionStore
import play.mvc.Controller
import play.mvc.Result
import play.libs.concurrent.HttpExecutionContext
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

import javax.inject.Inject
import org.pac4j.core.profile.CommonProfile
import org.pac4j.play.PlayWebContext
import org.pac4j.play.scala.{Security, SecurityComponents}
import org.webjars.play.WebJarsUtil

import scala.concurrent.ExecutionContext


class CallbackController @Inject()(val controllerComponents: SecurityComponents)
                                  (implicit ec: ExecutionContext, webjars: WebJarsUtil)
  extends Security[CommonProfile] {

  private val callbackLogic = new DefaultCallbackLogic[Result, PlayWebContext]

  def callback = Action { implicit request =>
    val playWebContext = new PlayWebContext(request, playSessionStore)

    callbackLogic.perform(playWebContext, config, config.getHttpActionAdapter[Result,PlayWebContext], defaultUrl, saveInSession, multiProfile, false, defaultClient), ec.current)
    Ok()
  }

  def callback(codeOpt: Option[String] = None) = Action.async {
    (for {
      code <- codeOpt
    } yield {
      getToken(code).flatMap { case (idToken, accessToken) =>
        getUser(accessToken).map { user =>
          Cache.set(idToken+ "profile", user)
          Redirect(routes.User.index())
            .withSession(
              "idToken" -> idToken,
              "accessToken" -> accessToken
            )
        }

      }.recover {
        case ex: IllegalStateException => Unauthorized(ex.getMessage)
      }
    }).getOrElse(Future.successful(BadRequest("No parameters supplied")))
  }

}
