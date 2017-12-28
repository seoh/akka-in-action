import akka.actor.{Actor, ActorRef}
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object BoxOffice {
  case class CreateEvent(name: String, tickets: Int)
  case class GetEvent(name: String)
  case object GetEvents

  case class GetTickets(event: String, tickets: Int)
  case class CancelEvent(name: String)

  case class Event(name: String, tickets: Int)
  case class Events(events: List[Event])

  sealed trait EventResponse
  case class EventCreated(event: Event) extends EventResponse
  case object EventExists extends EventResponse
}

class BoxOffice(implicit timeout: Timeout) extends Actor {
  import BoxOffice._

  // BoxOffice --> new TicketSeller (in current context)
  def createTicketSeller(name: String) =
    context.actorOf(TicketSeller.props(name), name)

  def receive = {
    case CreateEvent(name, tickets) =>
      def create = {
        val eventTickets = createTicketSeller(name)
        val newTickets = (1 to tickets).map(TicketSeller.Ticket).toList
        eventTickets ! TicketSeller.Add(newTickets)
        sender() ! EventCreated
      }

      context.child(name).fold(create)(_ => sender() ! EventExists)

    case GetTickets(event, tickets) =>
      def notFound = sender() ! TicketSeller.Tickets(event)
      def buy(child: ActorRef) = child.forward(TicketSeller.Buy(tickets))

      context.child(event).fold(notFound)(buy)

    case GetEvents =>
      import akka.pattern.ask
      import akka.pattern.pipe

      def getEvents: Iterable[Future[Option[Event]]] = 
        context.children map { child =>
          self.ask(GetEvent(child.path.name)).mapTo[Option[Event]]
        }

      def convertToEvents(f: Future[Iterable[Option[Event]]]) =
        f map (_.flatten) map (i => Events(i.toList))

      pipe(convertToEvents(Future.sequence(getEvents))) to sender()
  }
}
