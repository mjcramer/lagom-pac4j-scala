package controllers

import org.pac4j.core.client.IndirectClient
import org.pac4j.http.client.indirect.FormClient
import org.pac4j.jwt.profile.JwtGenerator
import play.api.mvc._
import org.pac4j.core.profile._
import org.pac4j.core.util.CommonHelper
import org.pac4j.play.PlayWebContext
import org.pac4j.play.scala._
import play.api.libs.json.Json
import org.pac4j.core.credentials.Credentials
import javax.inject.Inject
import org.pac4j.cas.profile.CasProxyProfile
import org.pac4j.core.context.Pac4jConstants
import org.pac4j.core.context.session.SessionStore
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration
import org.webjars.play.WebJarsUtil

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext

class MainController @Inject() (val controllerComponents: SecurityComponents)
                               (implicit ec: ExecutionContext, webjars: WebJarsUtil)
  extends Security[CommonProfile] {


  private def getProfiles(implicit request: RequestHeader): List[CommonProfile] = {
    val webContext = new PlayWebContext(request, playSessionStore)
    val profileManager = new ProfileManager[CommonProfile](webContext)
    val profiles = profileManager.getAll(true)
    asScalaBuffer(profiles).toList
  }

  def index = Secure("AnonymousClient", "csrfToken") { implicit request =>
    val webContext = new PlayWebContext(request, playSessionStore)
    val sessionStore = webContext.getSessionStore().asInstanceOf[SessionStore[PlayWebContext]]
    val sessionId = sessionStore.getOrCreateSessionId(webContext)
    val csrfToken = sessionStore.get(webContext, Pac4jConstants.CSRF_TOKEN).asInstanceOf[String]
    val formClient = config.getClients.findClient("FormClient").asInstanceOf[FormClient]

    Ok(views.html.index(formClient.getCallbackUrl, profiles, csrfToken, sessionId))
  }

  def csrfIndex = Secure("AnonymousClient", "csrfCheck") { implicit request =>
    Ok(views.html.csrf(profiles.asJava))
  }

//  def restricted = Secure { implicit request =>
//    val webContext = new PlayWebContext(request, playSessionStore)
//    val sessionStore = webContext.getSessionStore().asInstanceOf[SessionStore[PlayWebContext]]
//    val sessionId = sessionStore.getOrCreateSessionId(webContext)
//    val csrfToken = sessionStore.get(webContext, Pac4jConstants.CSRF_TOKEN).asInstanceOf[String]
//    Ok(views.html.index(profiles, csrfToken, sessionId))
//  }

  def login = Action { implicit request =>
    val formClient = config.getClients.findClient("FormClient").asInstanceOf[FormClient]
    Ok(views.html.login(formClient.getCallbackUrl))
  }


  //  def formIndex = Secure("FormClient") { implicit request =>
//    Ok(views.html.restricted(profiles))
//  }
//
//  // Setting the isAjax parameter is no longer necessary as AJAX requests are automatically detected:
//  // a 401 error response will be returned instead of a redirection to the login url.
//  def formIndexJson = Secure("FormClient") { implicit request =>
//    val content = views.html.restricted.render(profiles)
//    val json = Json.obj("content" -> content.toString())
//    Ok(json).as("application/json")
//  }
//
//  def basicauthIndex = Secure("IndirectBasicAuthClient") { implicit request =>
//    Ok(views.html.restricted(profiles))
//  }

//  def jwt = Action { request =>
//    val profiles = getProfiles(request)
//    val generator = new JwtGenerator[CommonProfile](new SecretSignatureConfiguration("12345678901234567890123456789012"))
//    var token: String = ""
//    if (CommonHelper.isNotEmpty(profiles.asJava)) {
//      token = generator.generate(profiles.asJava.get(0))
//    }
//    Ok(views.html.jwt.render(token))
//  }
//
//  def forceLogin = Action { request =>
//    val context: PlayWebContext = new PlayWebContext(request, playSessionStore)
//    val client = config.getClients.findClient(context.getRequestParameter(Pac4jConstants.DEFAULT_CLIENT_NAME_PARAMETER)).asInstanceOf[IndirectClient[Credentials,CommonProfile]]
//    Redirect(client.getRedirectAction(context).getLocation)
//  }
}
