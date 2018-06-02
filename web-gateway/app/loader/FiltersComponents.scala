package loader

import akka.stream.Materializer
import org.pac4j.play.store.PlayCacheSessionStore
import org.pac4j.core.config.Config
import play.api.Configuration

import scala.concurrent.ExecutionContext
import com.softwaremill.macwire._
import org.pac4j.play.filters.SecurityFilter
import play.api.mvc.EssentialFilter

trait FiltersComponents {

  def materializer: Materializer
  def configuration: Configuration
  def playSessionStore: PlayCacheSessionStore
  def pac4JConfig: Config
  implicit def executionContext: ExecutionContext

  lazy val securityFilter: SecurityFilter = wire[SecurityFilter]

  lazy val httpFilters: Seq[EssentialFilter] = Seq(securityFilter)

}
