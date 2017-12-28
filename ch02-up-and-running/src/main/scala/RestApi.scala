
import akka.actor.ActorSystem
import akka.util.Timeout

object RestApi {
  case class EventDescription(tickets: Int) { require(tickets > 0) }
  case class TicketRequest(tickets: Int) { require(tickets > 0) }
  case class Error(message: String)
}

class RestApi(system: ActorSystem, timeout: Timeout) {
  val routes = ""
}