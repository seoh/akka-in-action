import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.{ConfigFactory, ConfigValueFactory}

class Simple extends Actor {
  def receive = {
    case m => println(s"received $m")
  }
}

object Main extends App {
  val config = ConfigFactory.load()

  println(config.getString("akka.remote.netty.tcp.hostname"))
  println(config.getInt("akka.remote.netty.tcp.port"))
  val backend = ActorSystem("backend", config)

  val simple = backend.actorOf(Props[Simple], "simple")
}
