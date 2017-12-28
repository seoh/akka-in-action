import akka.actor.{Actor, PoisonPill}

object TicketSeller {
  case class Add(tickets: List[Ticket])
  case class Buy(tickets: Int)
  case class Ticket(id: Int)
  case class Tickets(event: String, entries: List[Ticket] = List.empty)
  case object GetEvent
  case object Cancel

  def props(name: String) = ???
}

class TicketSeller(event: String) extends Actor {
  import TicketSeller._

  var tickets = List.empty[Ticket]
  
  def receive = {
    case Add(newTickets) =>
      tickets = newTickets.reverse ++ tickets
    case Buy(nOfTickets) =>
      val entries = tickets.take(nOfTickets)
      if(entries.size >= nOfTickets) {
        sender() ! Tickets(event, entries)
        tickets = tickets.drop(nOfTickets)
      } else sender() ! Tickets(event)
    case GetEvent =>
      sender() ! Some(BoxOffice.Event(event, tickets.size))
    case Cancel =>
      sender() ! Some(BoxOffice.Event(event, tickets.size))
      self ! PoisonPill
  }
}
