import akka.actor.{ ActorSystem, ActorRef, Props }
import akka.event.Logging
import actors.BoxOffice

import com.typesafe.config.ConfigFactory

object BackendMain extends App with Startup {
  val config = ConfigFactory.load("backend") 
  implicit val system = ActorSystem("backend", config) 
  implicit val timeout = configuredRequestTimeout(config)

  system.actorOf(BoxOffice.props, BoxOffice.name)
}
