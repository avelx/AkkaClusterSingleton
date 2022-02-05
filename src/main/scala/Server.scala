import actors.Counter
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn
import akka.util.Timeout
import akka.actor.typed.scaladsl.AskPattern._
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.cluster.sharding.typed.scaladsl.{ClusterSharding, Entity, EntityRef, EntityTypeKey}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}

import scala.concurrent.duration._
import scala.util.Try

object Server extends App {

  val instanceId = java.util.UUID.randomUUID.toString
  implicit val timeout: Timeout = 3.seconds

  // ActorSystem[Nothing](IotSupervisor(), "iot-system")
  implicit val system = ActorSystem[Nothing](Behaviors.empty, "CounterSystem")
  implicit val executionContext = system.executionContext

  val portNumber = Try {
    args(0).toInt
  }.toOption.getOrElse(8080)

  val TypeKey = EntityTypeKey[Counter.Command]("Counter")

  val sharding = ClusterSharding(system)

  val shardRegion: ActorRef[ShardingEnvelope[Counter.Command]] =
    sharding.init(Entity(TypeKey)(createBehavior = entityContext => Counter(entityContext.entityId)))



  val route = concat(
    pathPrefix("increment") {
      path(Segment) { entityId =>
        shardRegion ! ShardingEnvelope(entityId, Counter.Increment)
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Incremented</h1>"))
      }
    },
    pathPrefix("counter") {
      path(Segment) { id =>
        val counter: EntityRef[Counter.Command] = sharding.entityRefFor(TypeKey, id)
        onSuccess(counter.ask(ref => Counter.GetValue(ref))) { value =>
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"<h1>Value: $value</h1>"))
        }
      }
    }
  )

  val bindingFuture = Http().newServerAt("localhost", portNumber).bind(route)

  println(s"Server now online. " +
    s"Please navigate to http://localhost:$portNumber/increment" +
    s"\nPress RETURN to stop...")

  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

}
