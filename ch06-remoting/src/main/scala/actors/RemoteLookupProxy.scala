package actors

import akka.actor.{ Actor, ActorLogging, ActorRef, Identify } // kind of actors
import akka.actor.{ ActorIdentity, ReceiveTimeout, Terminated } // actor status
import scala.concurrent.duration._

/**
  Why need Proxy?

  backend system or boxOffice actor is 
  - not yet started
  - or paused
  - or cannot be restarted


  type State = Identifited | Activated
 */
class RemoteLookupProxy(path: String) extends Actor with ActorLogging{
  context.setReceiveTimeout(3 seconds)
  sendIdentifyRequest()

  def sendIdentifyRequest(): Unit = {
    val selection = context.actorSelection(path)
    selection ! Identify(path)
  }

  def receive = identify

  def identify: Receive = {
    case ActorIdentity(`path`, Some(actor)) =>
      context.setReceiveTimeout(Duration.Undefined)
      log.info("switching to active state")
      context.become(active(actor))
      context.watch(actor)

    case ActorIdentity(`path`, None) =>
      log.error(s"Remote actor with path $path is not available.")

    case ReceiveTimeout =>
      sendIdentifyRequest()
    
    case msg: Any =>
      log.error(s"Igroring message $msg, not ready yet.")
  }

  def active(actor: ActorRef): Receive = {
    case Terminated(actorRef) =>
      log.info(s"Actor $actorRef terminated.")
      context.become(identify)
      log.info("switching to identify state")
      context.setReceiveTimeout(3 seconds)
      sendIdentifyRequest()
    
    case msg: Any =>
      actor forward msg
  }
}
