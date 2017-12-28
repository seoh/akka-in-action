import akka.actor.{ ActorSystem, Props }
import akka.testkit.{ ImplicitSender, TestActorRef, TestKit }
import org.scalatest.{ MustMatchers, WordSpecLike }

class EchoActorTest extends TestKit(ActorSystem("testsystem"))
  with WordSpecLike
  with ImplicitSender
  with StopSystemAfterAll  {

  "An EchoActor" must {
    "reply with the same message it receives without ask" in {
      val echo = system.actorOf(Props[EchoActor], "echo2")

      val message = "some message"
      echo ! message
      expectMsg(message)
    }
  }
}
