import akka.util.Timeout
import com.typesafe.config.Config

trait RequestTimeout {
  import scala.concurrent.duration._

  def requestTimeout(config: Config): Timeout = {
    val d = Duration(config.getString("akka.http.server.request-timeout"))
    FiniteDuration(d.length, d.unit)
  }
}
