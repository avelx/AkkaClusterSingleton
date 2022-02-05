package actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object Counter {
  val actorUUId : String = java.util.UUID.randomUUID.toString
  sealed trait Command

  case object Increment extends Command

  final case class GetValue(replyTo: ActorRef[Int]) extends Command

  def apply(entityId: String): Behavior[Command] = {
    Behaviors.setup[Command] { context =>
      context.setLoggerName("ch.qos.logback.core.ConsoleAppender")
      context.log.info("Starting up")

      def updated(value: Int): Behavior[Command] = {
        Behaviors.receiveMessage[Command] {
          case Increment =>
            context.log.info(s"Increment: ${value + 1} - $actorUUId")
            updated(value + 1)
          case GetValue(replyTo) =>
            context.log.info(s"GetValue: ${value + 1} - $actorUUId")
            replyTo ! value
            Behaviors.same
        }
      }

      updated(0)
    }
  }
}
