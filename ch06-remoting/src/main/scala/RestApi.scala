
import akka.actor.ActorRef
import akka.event.LoggingAdapter
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{ExecutionContext, Future}
//import akka.http.scaladsl.server.Directives.{get, post, delete }
//import akka.http.scaladsl.server.Directives.{ onSuccess, entity, as, Segment, pathPrefix }
import akka.http.scaladsl.server.Directives._
//import akka.http.scaladsl.server.Directive

trait RestApi extends BoxOfficeApi with EventMarshalling {
  import StatusCodes.{BadRequest, Created, NotFound, OK}
  import actors.BoxOffice

  val routes: Route = eventsRoute ~ eventRoute ~ ticketsRoute

  private val eventsRoute: Route =
    pathPrefix("events") {
      pathEndOrSingleSlash {
        get {
          // GET /events
          onSuccess(getEvents)(complete(OK, _))
        }
      }
    }

  private val eventRoute: Route =
    pathPrefix("events" / Segment) { event =>
      pathEndOrSingleSlash {
        post {
          // POST /events/:event
          entity(as[EventDescription]) { ed =>
            onSuccess(createEvent(event, ed.tickets)) {
              case BoxOffice.EventCreated(e) => complete(Created, e)
              case BoxOffice.EventExists =>
                val err = Error(s"$event event exists already.")
                complete(BadRequest, err)
            }
          }
        } ~
        get {
          // GET /events/:event
          onSuccess(getEvent(event)) {
            _.fold(complete(NotFound))(complete(OK, _))
          }
        } ~
        delete {
          // DELETE /events/:event
          onSuccess(cancelEvent(event)) {
            _.fold(complete(NotFound))(complete(OK, _))
          }
        }
      }
    }

  private val ticketsRoute: Route =
    pathPrefix("events" / Segment / "tickets") { event =>
      post {
        pathEndOrSingleSlash {
          // POST /events/:event/tickets
          entity(as[TicketRequest]) { request =>
            onSuccess(requestTickets(event, request.tickets)) { tickets =>
              if(tickets.entries.isEmpty) complete(NotFound)
              else complete(Created, tickets)
            }
          }
        }
      }
    }
}

trait BoxOfficeApi {
  import actors.BoxOffice._
  import actors.TicketSeller

  def log: LoggingAdapter
  def createBoxOffice(): ActorRef

  implicit def executionContext: ExecutionContext
  implicit def requestTimeout: Timeout

  lazy val boxOffice: ActorRef = createBoxOffice()

  def createEvent(event: String, nrOfTickets: Int): Future[EventResponse] = {
    log.info(s"Received new event $event, sending to $boxOffice")
    boxOffice.ask(CreateEvent(event, nrOfTickets)).mapTo[EventResponse]
  }

  def getEvents: Future[Events] =
    boxOffice.ask(GetEvents).mapTo[Events]

  def getEvent(event: String): Future[Option[Event]] =
    boxOffice.ask(GetEvent(event)).mapTo[Option[Event]]

  def cancelEvent(event: String): Future[Option[Event]] =
    boxOffice.ask(CancelEvent(event)).mapTo[Option[Event]]

  def requestTickets(event: String, tickets: Int): Future[TicketSeller.Tickets] =
    boxOffice.ask(GetTickets(event, tickets)).mapTo[TicketSeller.Tickets]
}
