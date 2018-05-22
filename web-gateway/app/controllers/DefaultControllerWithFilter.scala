package controllers

import javax.inject.Inject
import org.pac4j.core.context.Pac4jConstants
import org.pac4j.core.context.session.SessionStore
import org.pac4j.core.profile.{CommonProfile, ProfileManager}
import org.pac4j.core.util.CommonHelper
import org.pac4j.http.client.indirect.FormClient
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration
import org.pac4j.jwt.profile.JwtGenerator
import org.pac4j.play.PlayWebContext
import org.pac4j.play.scala.{Security, SecurityComponents}
import play.api.libs.json.Json
import play.api.mvc._
import play.api.Configuration
import play.api.i18n.{I18nSupport, MessagesApi}

import scala.collection.JavaConverters._

//class DefaultControllerWithFilter @Inject()(override val messagesApi: MessagesApi,
//                                            override val controllerComponents: SecurityComponents,
//                                            loginTemplate: views.html.login)
//  extends BaseController
//    with Security[CommonProfile]
//    with I18nSupport {
//
//  private def getProfiles(implicit request: RequestHeader): List[CommonProfile] = {
//    val webContext = new PlayWebContext(request, playSessionStore)
//    val profileManager = new ProfileManager[CommonProfile](webContext)
//    val profiles = profileManager.getAll(true)
//    asScalaBuffer(profiles).toList
//  }
//
//  def index = Secure("AnonymousClient", "csrfToken") { request =>
//    val webContext = new PlayWebContext(request, playSessionStore)
//    val csrfToken = webContext.getSessionStore().asInstanceOf[SessionStore[PlayWebContext]].get(webContext, Pac4jConstants.CSRF_TOKEN).asInstanceOf[String]
//    Ok(views.html.index(request.profiles, csrfToken, null))
//  }
//
//  def protectedIndex = Action { implicit request =>
//    Ok(views.html.protectedIndex(getProfiles(request)))
//  }
//
//  def protectedCustomIndex = Action { implicit request =>
//    Ok(views.html.protectedIndex(getProfiles(request)))
//  }
//
//  def formIndex = Action { implicit request =>
//    Ok(views.html.protectedIndex(getProfiles(request)))
//  }
//
//  // Setting the isAjax parameter is no longer necessary as AJAX requests are automatically detected:
//  // a 401 error response will be returned instead of a redirection to the login url.
//  def formIndexJson = Action { implicit request =>
//    val content = views.html.protectedIndex.render(getProfiles(request))
//    val json = Json.obj("content" -> content.toString())
//    Ok(json).as("application/json")
//  }
//
//  def basicauthIndex = Action { implicit request =>
//    Ok(views.html.protectedIndex(getProfiles(request)))
//  }
//
//  def restJwtIndex = Action { implicit request =>
//    Ok(views.html.protectedIndex(getProfiles(request)))
//  }
//
//  def loginForm = Action { implicit request =>
//    val formClient = config.getClients.findClient("FormClient").asInstanceOf[FormClient]
//    Ok(loginTemplate(formClient.getCallbackUrl))
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
//}
