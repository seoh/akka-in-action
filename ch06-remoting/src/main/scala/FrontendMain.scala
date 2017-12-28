import akka.actor.{ ActorSystem, ActorRef, Props }
import akka.event.Logging
import actors.{ BoxOffice, RemoteLookupProxy }

import com.typesafe.config.ConfigFactory

object FrontendMain extends App with Startup {
  val config = ConfigFactory.load("frontend") 
  implicit val system = ActorSystem("frontend", config) 

  val api = new RestApi() {
    val log = Logging(system.eventStream, "go-ticks")
    implicit val requestTimeout = configuredRequestTimeout(config)
    implicit def executionContext = system.dispatcher

    def createPath(): String = {
      val conf = config.getConfig("backend")
      val host = conf.getString("host")
      val port = conf.getInt("port")
      val protocol = conf.getString("protocol")
      val systemName = conf.getString("system")
      val actorName = conf.getString("actor")
      
      s"$protocol://$systemName@$host:$port/$actorName"
    }

    def createBoxOffice: ActorRef =
      system.actorOf(
        Props(new RemoteLookupProxy(createPath())), 
        "lookupBoxOffice"
      )
  }

  startup(api.routes)
}
