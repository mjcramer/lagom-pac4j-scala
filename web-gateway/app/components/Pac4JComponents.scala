package components

import java.io.File

import com.softwaremill.macwire._
import controllers.{CustomAuthorizer, DemoHttpActionAdapter, RoleAdminAuthGenerator}
import javax.inject.Provider
import org.pac4j.cas.client.CasClient
import org.pac4j.cas.config.{CasConfiguration, CasProtocol}
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer
import org.pac4j.core.client.Clients
import org.pac4j.core.client.direct.AnonymousClient
import org.pac4j.core.config.Config
import org.pac4j.core.credentials.UsernamePasswordCredentials
import org.pac4j.core.credentials.authenticator.Authenticator
import org.pac4j.core.matching.PathMatcher
import org.pac4j.core.profile.CommonProfile
import org.pac4j.http.client.direct.{DirectBasicAuthClient, ParameterClient}
import org.pac4j.http.client.indirect.{FormClient, IndirectBasicAuthClient}
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator
import org.pac4j.oauth.client.{FacebookClient, TwitterClient}
import org.pac4j.oidc.client.OidcClient
import org.pac4j.oidc.config.OidcConfiguration
import org.pac4j.oidc.profile.OidcProfile
import org.pac4j.play.{CallbackController, LogoutController}
import org.pac4j.play.scala.Pac4jScalaTemplateHelper
import org.pac4j.play.store.{PlayCacheSessionStore, PlaySessionStore}
import org.pac4j.saml.client.{SAML2Client, SAML2ClientConfiguration}
import play.api.cache.ehcache.EhCacheComponents
import play.cache.{AsyncCacheApi, DefaultAsyncCacheApi, DefaultSyncCacheApi, SyncCacheApi}
import org.ldaptive.{ConnectionConfig, DefaultConnectionFactory}
import org.ldaptive.auth.FormatDnResolver
import org.ldaptive.auth.PooledBindAuthenticationHandler
import org.ldaptive.pool.BlockingConnectionPool
import org.ldaptive.pool.IdlePruneStrategy
import org.ldaptive.pool.PoolConfig
import org.ldaptive.pool.SearchValidator
import org.pac4j.ldap.profile.service.LdapProfileService

/**
 * Provides Pac4J components.
 *
 * There are some places where Pac4J is not fully dependency injection friendly, then
 * we have to manually set some values after object creating. Most of these places are
 * setting primitive values though, so maybe it is a matter of having better defaults?
 */
trait Pac4JComponents extends EhCacheComponents {

  // Cache APIS are required by org.pac4j.play.store.PlayCacheSessionStore
  lazy val asyncCacheApi: AsyncCacheApi = new DefaultAsyncCacheApi(defaultCacheApi)
  lazy val syncCacheApi: SyncCacheApi = wire[DefaultSyncCacheApi]

  // This is then used by Pac4jScalaTemplateHelper instance below
  def playSessionStore: PlayCacheSessionStore = wire[PlayCacheSessionStore]

  lazy val scalaTemplateHelper: Pac4jScalaTemplateHelper[CommonProfile] = wire[Pac4jScalaTemplateHelper[CommonProfile]]

  // FormClient is used for logins submitted through a form
  lazy val formClient: FormClient = {
    val baseUrl = configuration.get[String]("baseUrl")
    new FormClient(baseUrl + "/login", new SimpleTestUsernamePasswordAuthenticator())
  }

  // callbackController and logoutController are created manually in the Guice module.
  lazy val callbackController: CallbackController = {
    val callbackController = new CallbackController()
    callbackController.setDefaultUrl("/?defaulturlafterlogout")
    callbackController.setMultiProfile(true)
    callbackController
  }

  // We need this provider because Play router generates controllers accepting providers
  lazy val callbackControllerProvider: Provider[CallbackController] = new Provider[CallbackController] {
    override def get(): CallbackController = callbackController
  }

  lazy val logoutController: LogoutController = {
    val logoutController = new LogoutController()
    logoutController.setDefaultUrl("/")
    logoutController
  }

  lazy val logoutControllerProvider: Provider[LogoutController] = new Provider[LogoutController] {
    override def get(): LogoutController = logoutController
  }

//  // From here is where things got a little complicated because there are a lot of
//  // pac4j internal components. The good thing is that if something changes, Macwire
//  // will let us know.
//  lazy val facebookClient: FacebookClient = {
//    // configuration is provided by EhCacheComponents
//    val fbId = configuration.get[String]("fbId")
//    val fbSecret = configuration.get[String]("fbSecret")
//    new FacebookClient(fbId, fbSecret)
//  }
//
//  // The values were hardcoded in the Guice module, but of course they can be configured.
//  lazy val twitterClient: TwitterClient = new TwitterClient("HVSQGAw2XmiwcKOTvZFbQ", "FSiO9G9VRR4KCuksky0kgGuo8gAVndYymr4Nl7qc8AA")
//
//  // This was being created over and over, but with Macwire we can just wire it once
//  // and use later.
//  lazy val authenticator: Authenticator[UsernamePasswordCredentials] = wire[SimpleTestUsernamePasswordAuthenticator]
//  lazy val indirectBasicAuthClient: IndirectBasicAuthClient = wire[IndirectBasicAuthClient]
//
//  lazy val casConfiguration: CasConfiguration = {
//    val casConfiguration = new CasConfiguration("https://casserverpac4j.herokuapp.com/login")
//    //val casConfiguration = new CasConfiguration("http://localhost:8888/cas/login")
//    casConfiguration.setProtocol(CasProtocol.CAS20)
//    casConfiguration
//  }
//
//  lazy val casClient: CasClient = wire[CasClient]
//
//  lazy val sAML2ClientConfiguration: SAML2ClientConfiguration = {
//    val cfg = new SAML2ClientConfiguration("resource:samlKeystore.jks", "pac4j-demo-passwd", "pac4j-demo-passwd", "resource:openidp-feide.xml")
//    cfg.setMaximumAuthenticationLifetime(3600)
//    cfg.setServiceProviderEntityId("urn:mace:saml:pac4j.org")
//
//    // It would be better to use Play's "environment" here, but let's keep things
//    // like the original example using Guice.
//    cfg.setServiceProviderMetadataPath(new File("target", "sp-metadata.xml").getAbsolutePath)
//    cfg
//  }
//  lazy val saml2Client: SAML2Client = wire[SAML2Client]
//
//  lazy val oidcClient: OidcClient[OidcProfile, OidcConfiguration] = {
//    val oidcConfiguration = new OidcConfiguration()
//
//    // More and more values that could be part of the configuration
//    oidcConfiguration.setClientId("343992089165-i1es0qvej18asl33mvlbeq750i3ko32k.apps.googleusercontent.com")
//    oidcConfiguration.setSecret("unXK_RSCbCXLTic2JACTiAo9")
//    oidcConfiguration.setDiscoveryURI("https://accounts.google.com/.well-known/openid-configuration")
//    oidcConfiguration.addCustomParam("prompt", "consent")
//    val oidcClient = new OidcClient[OidcProfile, OidcConfiguration](oidcConfiguration)
//    oidcClient.addAuthorizationGenerator(new RoleAdminAuthGenerator)
//    oidcClient
//  }
//
//  lazy val parameterClient: ParameterClient = {
//    val jwtAuthenticator = new JwtAuthenticator()
//    jwtAuthenticator.addSignatureConfiguration(new SecretSignatureConfiguration("12345678901234567890123456789012"))
//    val parameterClient = new ParameterClient("token", jwtAuthenticator)
//    parameterClient.setSupportGetRequest(true)
//    parameterClient.setSupportPostRequest(false)
//    parameterClient
//  }
//
//  lazy val directBasicAuthClient: DirectBasicAuthClient = wire[DirectBasicAuthClient]


//  lazy val ldapProfileService = {
//    val dnResolver = new FormatDnResolver
//    dnResolver.setFormat(configuration.get[String]("ldap.CN ")+ "=%s," + configuration.get[String]("ldap..baseDN"))
//    val connectionConfig = new ConnectionConfig()
//    connectionConfig.setConnectTimeout(configuration.get500)
//    connectionConfig.setResponseTimeout(1000)
//    connectionConfig.setLdapUrl("ldap://localhost:" + LdapServer.PORT)
//    val connectionFactory = new DefaultConnectionFactory
//    connectionFactory.setConnectionConfig(connectionConfig)
//    val poolConfig = new PoolConfig
//    poolConfig.setMinPoolSize(1)
//    poolConfig.setMaxPoolSize(2)
//    poolConfig.setValidateOnCheckOut(true)
//    poolConfig.setValidateOnCheckIn(true)
//    poolConfig.setValidatePeriodically(false)
//    val searchValidator = new SearchValidator
//    val pruneStrategy = new IdlePruneStrategy
//    val connectionPool = new BlockingConnectionPool
//    connectionPool.setPoolConfig(poolConfig)
//    connectionPool.setBlockWaitTime(1000)
//    connectionPool.setValidator(searchValidator)
//    connectionPool.setPruneStrategy(pruneStrategy)
//    connectionPool.setConnectionFactory(connectionFactory)
//    connectionPool.initialize()
//    val pooledConnectionFactory = new Nothing
//    pooledConnectionFactory.setConnectionPool(connectionPool)
//    val handler = new PooledBindAuthenticationHandler
//    handler.setConnectionFactory(pooledConnectionFactory)
//    val ldaptiveAuthenticator = new Nothing
//    ldaptiveAuthenticator.setDnResolver(dnResolver)
//    ldaptiveAuthenticator.setAuthenticationHandler(handler)
//    // pac4j:
//    val ldapProfileService = new LdapProfileService(connectionFactory, ldaptiveAuthenticator)
//  }



  lazy val pac4JConfig: Config = {
    val baseUrl = configuration.get[String]("baseUrl")

    val clients = new Clients(baseUrl + "/callback",
      formClient,
      new AnonymousClient()
    )
//      facebookClient, twitterClient,
//      indirectBasicAuthClient, casClient, saml2Client, oidcClient,
    // parameterClient, directBasicAuthClient,

    val config = new Config(clients)
//    config.addAuthorizer("admin", new RequireAnyRoleAuthorizer[Nothing]("ROLE_ADMIN"))
//    config.addAuthorizer("custom", new CustomAuthorizer)
//    config.addMatcher("excludedPath", new PathMatcher().excludeRegex("^/facebook/notprotected\\.html$"))
    config.setHttpActionAdapter(new DemoHttpActionAdapter())
    config
  }
}

