import akka.actor.{ ActorSystem, Props }
import akka.testkit.{ CallingThreadDispatcher, EventFilter, TestActorRef, TestKit }
import com.typesafe.config.ConfigFactory

import org.scalatest.{ MustMatchers, WordSpecLike }

class Greeter01Test extends TestKit(Greeter01Test.testSystem)
  with WordSpecLike
  with StopSystemAfterAll {

  "The Greeter" must {
    "say Hello World! when a Greeting(\"World\") is sent to it" in {
      val dispatcherId = CallingThreadDispatcher.Id
      val props = Props[Greeter].withDispatcher(dispatcherId)
      val greeter = system.actorOf(props)

      EventFilter.info(
        message = "Hello World!",
        occurrences = 1
      ).intercept {
        greeter ! Greeting("World!")
      }
    }
  }
}

object Greeter01Test {
  val testSystem = {
    val config = ConfigFactory.parseString(
      """
      | akka.loggers = [akka.testkit.TestEventListener]
      """.stripMargin
    )

    ActorSystem("testsystem", config)
  }
}
