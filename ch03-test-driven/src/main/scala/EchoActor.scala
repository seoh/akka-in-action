import akka.actor.{ Actor, ActorLogging, ActorRef, Props }


class EchoActor extends Actor {

  def receive = {
    case message => sender ! message
  }
}
