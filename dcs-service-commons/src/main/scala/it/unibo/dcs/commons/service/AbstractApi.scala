package it.unibo.dcs.commons.service

import io.vertx.scala.ext.web.client.{HttpResponse, WebClient}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

abstract class AbstractApi(private[this] val discovery: HttpEndpointDiscovery,
                           private[this] val serviceName: String)(implicit executor: ExecutionContext) {

  private[this] var clientOption: Option[WebClient] = None

  discoverClient()

  private[this] def discoverClient(): Unit = {
    discovery.getWebClient(serviceName).onComplete {
      case Success(c) => clientOption = Some(c)
      case Failure(t) => ()
    }
  }

  protected final def request[T](action: WebClient => Future[HttpResponse[T]]): Future[HttpResponse[T]] =
    clientOption match {
      case Some(c) => action(c)
        .andThen {
          case Failure(_) =>
            clientOption = None
            discoverClient()
        }
      // TODO: replace with a more meaningful exception
      case _ => throw new RuntimeException
    }

}
