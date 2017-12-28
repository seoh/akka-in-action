import akka.actor.{ Actor, ActorRef, Props }

class SendingActor(receiver: ActorRef) extends Actor {
  import SendingActor._

  var internalState = List.empty[String]

  def receive = {
    case SortEvents(unsorted) =>
      receiver ! SortEvents(unsorted.sortBy(_.id))
  }

}


object SendingActor {
  def props(receiver: ActorRef) = 
    Props(new SendingActor(receiver))

  case class Event(id: Long)
  case class SortEvents(unsorted: List[Event])
}
