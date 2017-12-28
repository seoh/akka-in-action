import akka.actor.{ ActorSystem, Props }
import akka.testkit.{ CallingThreadDispatcher, EventFilter, TestActorRef, TestKit }
import com.typesafe.config.ConfigFactory

import org.scalatest.{ MustMatchers, WordSpecLike }

class Greeter02Test extends TestKit(ActorSystem("testsystem"))
  with WordSpecLike
  with StopSystemAfterAll {

  "The Greeter" must {
    "say Hello World! when a Greeting(\"World\") is sent to it" in {
      val props = Greeter02.props(Some(testActor))
      val greeter = system.actorOf(props, "greeter02-1")
      greeter ! Greeting("World")

      expectMsg("Hello World!")
    }
  }
}
