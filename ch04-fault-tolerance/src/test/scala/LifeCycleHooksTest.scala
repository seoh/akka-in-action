import akka.actor.{ ActorSystem, Props }
import akka.testkit.{ ImplicitSender, TestKit }
import org.scalatest.{ MustMatchers, WordSpecLike }

class LifeCycleHooksTest extends TestKit(ActorSystem("testsystem"))
  with WordSpecLike
  with ImplicitSender {

  "A LifeCycleHooks" must {
    "let it crash" in {
      val testActorRef = system.actorOf(Props[LifeCycleHooks], "LifeCycleHooks")
      testActorRef ! "restart"
      testActorRef.tell("msg", testActor)
      expectMsg("msg")

      system.stop(testActorRef)
      Thread.sleep(1000)
    }
  }

}
