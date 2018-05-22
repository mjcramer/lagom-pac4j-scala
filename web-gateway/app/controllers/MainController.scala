package controllers

import javax.inject.Inject
import org.pac4j.core.context.Pac4jConstants
import org.pac4j.core.context.session.SessionStore
import org.pac4j.core.profile._
import org.pac4j.core.util.CommonHelper
import org.pac4j.http.client.indirect.FormClient
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration
import org.pac4j.jwt.profile.JwtGenerator
import org.pac4j.play.PlayWebContext
import org.pac4j.play.scala._
import org.webjars.play.WebJarsUtil
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc._

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext

class MainController @Inject()(override val messagesApi: MessagesApi,
                               override val controllerComponents: ControllerComponents)
                              (implicit ec: ExecutionContext, webjars: WebJarsUtil)
  extends BaseController
//  with Security[CommonProfile]
  with I18nSupport {

//  private def getProfiles(implicit request: RequestHeader): List[CommonProfile] = {
//    val webContext = new PlayWebContext(request, playSessionStore)
//    val profileManager = new ProfileManager[CommonProfile](webContext)
//    val profiles = profileManager.getAll(true)
//    asScalaBuffer(profiles).toList
//  }

  def index = Action { implicit request =>
//    val webContext = new PlayWebContext(request, playSessionStore)
//    val sessionStore = webContext.getSessionStore().asInstanceOf[SessionStore[PlayWebContext]]
//    val sessionId = sessionStore.getOrCreateSessionId(webContext)
//    val csrfToken = sessionStore.get(webContext, Pac4jConstants.CSRF_TOKEN).asInstanceOf[String]
//    Ok(views.html.index(profiles, csrfToken, sessionId))
//    Ok(indexTemplate())
    Ok(views.html.index())
  }


  def login = Action { implicit request =>
    val formClient = new FormClient("http://locahost:9000/login", new SimpleTestUsernamePasswordAuthenticator())
    Ok(views.html.login(formClient.getLoginUrl))
  }


}
