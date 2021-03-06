
import spray.json.DefaultJsonProtocol

case class EventDescription(tickets: Int) { require(tickets > 0) }
case class TicketRequest(tickets: Int) { require(tickets > 0) }
case class Error(message: String)

trait EventMarshalling extends DefaultJsonProtocol {
  import actors.BoxOffice
  import actors.TicketSeller

  implicit val eventDescriptionFormat = jsonFormat1(EventDescription)
  implicit val eventFormat = jsonFormat2(BoxOffice.Event)
  implicit val eventsFormat = jsonFormat1(BoxOffice.Events)
  implicit val ticketRequestFormat = jsonFormat1(TicketRequest)
  implicit val ticketFormat = jsonFormat1(TicketSeller.Ticket)
  implicit val ticketsFormat = jsonFormat2(TicketSeller.Tickets)
  implicit val errorFormat = jsonFormat1(Error)
}
