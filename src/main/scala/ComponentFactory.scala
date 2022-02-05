import actors.Counter
import actors.Counter.Command
import akka.actor.TypedActor
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.cluster.sharding.typed.scaladsl.{ClusterSharding, Entity, EntityTypeKey}
import akka.remote.transport.TestTransport.Behavior
import org.slf4j.LoggerFactory

object ComponentFactory {
  val actorInstanceId = java.util.UUID.randomUUID.toString

//  val sys = ActorSystem(Counter(actorInstanceId), "Counter")
//  val counter: ActorRef[Command] = sys


}
