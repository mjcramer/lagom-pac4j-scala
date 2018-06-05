package controllers

import org.pac4j.core.profile.{CommonProfile, ProfileManager}
import org.pac4j.core.util.CommonHelper
import org.pac4j.http.client.indirect.FormClient
import org.pac4j.jwt.profile.JwtGenerator
import org.pac4j.play.PlayWebContext
import org.pac4j.play.scala.{Security, SecurityComponents}
import play.api.libs.json.Json
import javax.inject.Inject
import org.pac4j.core.context.Pac4jConstants
import org.pac4j.core.context.session.SessionStore
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration
import play.api.mvc._

import scala.collection.JavaConverters._

class MainControllerWithFilter @Inject() (val controllerComponents: SecurityComponents) extends Security[CommonProfile] {

//  def index = Secure("AnonymousClient", "csrfToken") { request =>
//    val webContext = new PlayWebContext(request, playSessionStore)
//    val csrfToken = webContext.getSessionStore().asInstanceOf[SessionStore[PlayWebContext]].get(webContext, Pac4jConstants.CSRF_TOKEN).asInstanceOf[String]
//    Ok(views.html.index(request.profiles, csrfToken, null))
//  }
//
//  def facebookIndex = Action { implicit request =>
//    Ok(views.html.restricted(getProfiles(request)))
//  }
//
//  def facebookAdminIndex = Action { implicit request =>
//    Ok(views.html.restricted(getProfiles(request)))
//  }
//
//  def facebookCustomIndex = Action { implicit request =>
//    Ok(views.html.restricted(getProfiles(request)))
//  }
//
//  def twitterIndex = Action { implicit request =>
//    Ok(views.html.restricted(getProfiles(request)))
//  }
//
//  def protectedIndex = Action { implicit request =>
//    Ok(views.html.restricted(getProfiles(request)))
//  }
//
//  def protectedCustomIndex = Action { implicit request =>
//    Ok(views.html.restricted(getProfiles(request)))
//  }
//
//  def formIndex = Action { implicit request =>
//    Ok(views.html.restricted(getProfiles(request)))
//  }
//
//  // Setting the isAjax parameter is no longer necessary as AJAX requests are automatically detected:
//  // a 401 error response will be returned instead of a redirection to the login url.
//  def formIndexJson = Action { implicit request =>
//    val content = views.html.restricted.render(getProfiles(request))
//    val json = Json.obj("content" -> content.toString())
//    Ok(json).as("application/json")
//  }
//
//  def basicauthIndex = Action { implicit request =>
//    Ok(views.html.restricted(getProfiles(request)))
//  }
//
//  def dbaIndex = Action { implicit request =>
//    Ok(views.html.restricted(getProfiles(request)))
//  }
//
//  def casIndex = Action { implicit request =>
//    Ok(views.html.restricted(getProfiles(request)))
//  }
//
//  def samlIndex = Action { implicit request =>
//    Ok(views.html.restricted(getProfiles(request)))
//  }
//
//  def oidcIndex = Action { implicit request =>
//    Ok(views.html.restricted(getProfiles(request)))
//  }
//
//  def restJwtIndex = Action { implicit request =>
//    Ok(views.html.restricted(getProfiles(request)))
//  }
//
//  def loginForm = Action { implicit request =>
//    val formClient = config.getClients.findClient("FormClient").asInstanceOf[FormClient]
//    Ok(views.html.login.render(formClient.getCallbackUrl))
//  }
//
//  def jwt = Action { implicit request =>
//    val profiles = getProfiles(request)
//    val generator = new JwtGenerator[CommonProfile](new SecretSignatureConfiguration("12345678901234567890123456789012"))
//    var token: String = ""
//    if (CommonHelper.isNotEmpty(profiles.asJava)) {
//      token = generator.generate(profiles.asJava.get(0))
//    }
//    Ok(views.html.jwt.render(token))
//  }
}
