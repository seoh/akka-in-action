import akka.actor.{ Actor, ActorRef }

class SilentActor extends Actor {
  import SilentActor._

  var internalState = List.empty[String]

  def receive = {
    case SilentMessage(data) => internalState = data :: internalState
    case GetState(receiver) => receiver ! state
  }

  def state = internalState.reverse
}

object SilentActor {
  case class SilentMessage(data: String)
  case class GetState(receiver: ActorRef)
}
