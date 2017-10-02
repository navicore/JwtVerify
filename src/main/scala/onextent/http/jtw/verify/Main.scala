package onextent.http.jtw.verify

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import onextent.http.jtw.verify.models.JsonSupport
import onextent.http.jtw.verify.routes.JwtRoute

import scala.concurrent.ExecutionContextExecutor

object Main extends LazyLogging with JsonSupport with ErrorSupport {

  def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem("Jwtverify-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val route =
      HealthCheck ~
      JwtRoute.apply

    Http().bindAndHandle(route, "0.0.0.0", port)
  }
}

