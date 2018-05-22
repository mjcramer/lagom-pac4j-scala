package modules

import java.time.Duration

import com.google.inject.{AbstractModule, Provides}
import controllers.{CustomAuthorizer, DemoHttpActionAdapter}
import org.ldaptive.auth.{Authenticator, FormatDnResolver, PooledBindAuthenticationHandler}
import org.ldaptive.pool._
import org.ldaptive.{ConnectionConfig, DefaultConnectionFactory}
import org.pac4j.core.client.Clients
import org.pac4j.http.client.direct.{DirectBasicAuthClient, ParameterClient}
import org.pac4j.http.client.indirect.{FormClient, IndirectBasicAuthClient}
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator
import org.pac4j.ldap.profile.service.LdapProfileService
import org.pac4j.play.{CallbackController, LogoutController}
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer
import org.pac4j.core.client.direct.AnonymousClient
import org.pac4j.core.config.Config
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration
import org.pac4j.play.scala.{DefaultSecurityComponents, SecurityComponents}
import org.pac4j.play.store.{PlayCacheSessionStore, PlaySessionStore}
import play.api.{Logger, Configuration, Environment}

class SecurityModule(environment: Environment, configuration: Configuration) {

  println("Initializing security module...")
  val baseUrl = configuration.get[String]("ldap.baseUrl")


//  override def configure(): Unit = {
//
//    println("*** Start configuring... ")
//
//    bind(classOf[PlaySessionStore]).to(classOf[PlayCacheSessionStore])
//    bind(classOf[SecurityComponents]).to(classOf[DefaultSecurityComponents])
//
//    // callback
//    val callbackController = new CallbackController()
//    callbackController.setDefaultUrl("/?defaulturlafterlogout")
//    callbackController.setMultiProfile(true)
//    bind(classOf[CallbackController]).toInstance(callbackController)
//
//    // logout
//    val logoutController = new LogoutController()
//    logoutController.setDefaultUrl("/")
//    bind(classOf[LogoutController]).toInstance(logoutController)
//
//    println("*** Finish configuring... ")
//  }

//
//  @Singleton
//  class UIController @Inject()(environment: Environment) extends Controller with Logging {
//
//    val dnResolver = new NoOpDnResolver()
//
//    val connConfig = new ConnectionConfig()
//    connConfig.setConnectTimeout(Duration.ofMillis(500))
//    connConfig.setResponseTimeout(Duration.ofMillis(1000))
//    connConfig.setLdapUrl("ldap://localhost:389")
//
//    val connFactory = new DefaultConnectionFactory()
//    connFactory.setConnectionConfig(connConfig)
//
//    val poolConfig = new PoolConfig()
//    poolConfig.setMinPoolSize(1)
//    poolConfig.setMaxPoolSize(2)
//    poolConfig.setValidateOnCheckOut(true)
//    poolConfig.setValidateOnCheckIn(true)
//    poolConfig.setValidatePeriodically(false)
//
//    val searchValidator = new SearchValidator()
//    val pruneStrategy   = new IdlePruneStrategy()
//
//    val connPool = new BlockingConnectionPool()
//    connPool.setPoolConfig(poolConfig)
//    connPool.setBlockWaitTime(Duration.ofMillis(1000))
//    connPool.setValidator(searchValidator)
//    connPool.setPruneStrategy(pruneStrategy)
//    connPool.setConnectionFactory(connFactory)
//    connPool.initialize()
//
//    val pooledConnectionFactory = new PooledConnectionFactory()
//    pooledConnectionFactory.setConnectionPool(connPool)
//
//    val handler = new PooledBindAuthenticationHandler
//    handler.setConnectionFactory(pooledConnectionFactory)
//
//    val authenticator = new Authenticator()
//    authenticator.setDnResolver(dnResolver)
//    authenticator.setAuthenticationHandler(handler)
//
//    val ldapProfileService = new LdapProfileService(connFactory, authenticator, "dc=example,dc=org")
//
//    def index = Action {
//
//      val user        = new User("cn=john,dc=example,dc=org")
//      val credentials = new Credential("password")
//
//      val request = new AuthenticationRequest(user, credentials)
//
//      try {
//        val response = ldapProfileService.getLdapAuthenticator.authenticate(request)
//        if (response.getAuthenticationResultCode == AuthenticationResultCode.AUTHENTICATION_HANDLER_SUCCESS) {
//          Ok(views.html.index())
//        } else {
//          log.error("Ldap authentication failed.")
//          Unauthorized
//        }
//      } catch {
//        case ex: LdapException =>
//          log.error("Exception during Ldap authentication.", ex)
//          Unauthorized
//      }
//    }
//  }
//
//
//  DirectBasicAuthClient(ldapProfileService);.
//



  // custom LDAP service
//  private def getLdapProfileService: LdapProfileService = {
//
//    println("*** Start getting LDAP profile service...")
//
//    val dnResolver: FormatDnResolver = new FormatDnResolver()
//    dnResolver.setFormat(configuration.get[String]("ldap.dnResolverFormat"))
//
//    val connectionConfig: ConnectionConfig = new ConnectionConfig
//    connectionConfig.setConnectTimeout(Duration.ofSeconds(500))
//    connectionConfig.setResponseTimeout(Duration.ofSeconds(1000))
//    connectionConfig.setLdapUrl(configuration.get[String]("ldap.baseUrl"))
//
//    val connectionFactory: DefaultConnectionFactory = new DefaultConnectionFactory
//    connectionFactory.setConnectionConfig(connectionConfig)
//
//    val poolConfig: PoolConfig = new PoolConfig()
//    poolConfig.setMinPoolSize(1)
//    poolConfig.setMaxPoolSize(2)
//    poolConfig.setValidateOnCheckOut(true)
//    poolConfig.setValidateOnCheckIn(true)
//    poolConfig.setValidatePeriodically(false)
//
//    val searchValidator: SearchValidator = new SearchValidator()
//    val pruneStrategy: IdlePruneStrategy = new IdlePruneStrategy()
//    val connectionPool: BlockingConnectionPool = new BlockingConnectionPool
//    connectionPool.setPoolConfig(poolConfig)
//    connectionPool.setBlockWaitTime(Duration.ofMinutes(1000))
//    connectionPool.setValidator(searchValidator)
//    connectionPool.setPruneStrategy(pruneStrategy)
//    connectionPool.setConnectionFactory(connectionFactory)
//    connectionPool.initialize()
//
//    val pooledConnectionFactory: PooledConnectionFactory = new PooledConnectionFactory
//    pooledConnectionFactory.setConnectionPool(connectionPool)
//
//    val handler: PooledBindAuthenticationHandler = new PooledBindAuthenticationHandler
//    handler.setConnectionFactory(pooledConnectionFactory)
//
//    val ldaptiveAuthenticator: Authenticator = new Authenticator
//    ldaptiveAuthenticator.setDnResolver(dnResolver)
//    ldaptiveAuthenticator.setAuthenticationHandler(handler)
//
//    // pac4j:
//    val ldapProfileService = new LdapProfileService(
//      connectionFactory,
//      ldaptiveAuthenticator,
//      this.configuration.get[String]("ldap.attributesLdap"),
//      this.configuration.get[String]("ldap.dnResolverFormat")
//    )
//    ldapProfileService.setUsernameAttribute(configuration.get[String]("ldap.usernameAttributeLdap"))
//
//    println("*** Finish getting LDAP profile service...")
//
//    ldapProfileService
//  }

  /** TODO: Renable this when we get LDAP working properly...
  def provideFormClient: FormClient = new FormClient(baseUrl + "/loginForm", getLdapProfileService)
  **/

  @Provides
  def provideFormClient = {

    println("*** Start getting form client...")

    val formClient = new FormClient(baseUrl + "/login", new SimpleTestUsernamePasswordAuthenticator())

    println("*** Finish getting form client...")

    formClient
  }

//  @Provides
//  def provideIndirectBasicAuthClient: IndirectBasicAuthClient = new IndirectBasicAuthClient(new SimpleTestUsernamePasswordAuthenticator())

//  @Provides
//  def provideParameterClient: ParameterClient = {
//    val jwtAuthenticator = new JwtAuthenticator()
//    jwtAuthenticator.addSignatureConfiguration(new SecretSignatureConfiguration("12345678901234567890123456789012"))
//    val parameterClient = new ParameterClient("token", jwtAuthenticator)
//    parameterClient.setSupportGetRequest(true)
//    parameterClient.setSupportPostRequest(false)
//    parameterClient
//  }
//
//  @Provides
//  def provideDirectBasicAuthClient: DirectBasicAuthClient = new DirectBasicAuthClient(new SimpleTestUsernamePasswordAuthenticator)
//

//  @Provides
//  def provideConfig(formClient: FormClient, indirectBasicAuthClient: IndirectBasicAuthClient,
//                    parameterClient: ParameterClient, directBasicAuthClient: DirectBasicAuthClient): Config = {
//    val clients = new Clients(baseUrl + "/callback",
//      formClient,
//      indirectBasicAuthClient,
//      parameterClient,
//      directBasicAuthClient,
//      new AnonymousClient()
//    )
//
//    val config = new Config(clients)
//    config.addAuthorizer("admin", new RequireAnyRoleAuthorizer[Nothing]("ROLE_ADMIN"))
//    config.addAuthorizer("custom", new CustomAuthorizer)
//    config.setHttpActionAdapter(new DemoHttpActionAdapter())
//    config
//  }
}
