import akka.actor.{ Actor, ActorLogging, ActorRef, Props }


class Greeter02(listener: Option[ActorRef]) extends Actor with ActorLogging {

  def receive = {
    case Greeting(who) =>
      val message = s"Hello $who!"
      log.info(message)
      listener map (_ ! message)
  }
}

object Greeter02 {
  def props(listener: Option[ActorRef]) = Props(new Greeter02(listener))
}
