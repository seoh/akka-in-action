import akka.actor.{ ActorSystem, Props }
import akka.testkit.{ TestActorRef, TestKit }
import org.scalatest.{ MustMatchers, WordSpecLike }
import scala.util.Random

class SendingActorTest extends TestKit(ActorSystem("testsystem"))
  with WordSpecLike
  with MustMatchers
  with StopSystemAfterAll  {

  "A Sending Actor" must {
    "send a message to another actor when it has finished propcessing" in {
      import SendingActor._
      val props = SendingActor.props(testActor)
      val sendingActor = system.actorOf(props, "sendingActor")

      val size = 1000
      val maxInclusive = 100000

      def randomEvents() = (0 until size) map { _ => 
        Event(Random.nextInt(maxInclusive))
      } toList

      val unsorted = randomEvents()
      val sorted = SortEvents(unsorted)
      sendingActor ! sorted
      expectMsgPF() {
        case SortEvents(events) =>
          events.size must be (size)
          unsorted.sortBy(_.id) must be (events)
      }
    }
  }
}
