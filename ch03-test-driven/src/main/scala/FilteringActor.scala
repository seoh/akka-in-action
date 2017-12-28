import akka.actor.{ Actor, ActorRef, Props }

class FilteringActor(receiver: ActorRef, bufferSize: Int) extends Actor {
  import FilteringActor._

  var lastMessage = List.empty[Event]

  def receive = {
    case msg: Event =>
      if (!lastMessage.contains(msg)) {
        lastMessage = msg :: lastMessage
        receiver ! msg

        if (lastMessage.size > bufferSize)
          lastMessage = lastMessage.init
      }
  }
}


object FilteringActor {
  def props(receiver: ActorRef, bufferSize: Int) = 
    Props(new FilteringActor(receiver, bufferSize))

  case class Event(id: Long)
}
