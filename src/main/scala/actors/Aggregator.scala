package actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import utils.SerializableMessage

object Aggregator {
  val actorUUId : String = java.util.UUID.randomUUID.toString
  sealed trait AggCmd extends SerializableMessage

  case object Add extends AggCmd
  final case class GetStatus(replyTo: ActorRef[Int]) extends AggCmd

  def apply(): Behavior[AggCmd] = {
    Behaviors.setup[AggCmd] { context =>
      context.setLoggerName("ch.qos.logback.core.ConsoleAppender")
      context.log.info("Starting up")

      def updated(value: Int): Behavior[AggCmd] = {
        Behaviors.receiveMessage[AggCmd] {
          case Add =>
            context.log.info(s"Add: ${value + 1} - $actorUUId")
            updated(value + 1)
          case GetStatus(replyTo) =>
            context.log.info(s"GetStatus: ${value + 1} - $actorUUId")
            replyTo ! value
            Behaviors.same
        }
      }

      updated(0)
    }
  }
}
