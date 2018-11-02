package it.unibo.dcs.service.webapp.verticles.handler.impl.subscribers

import io.vertx.lang.scala.json.JsonArray
import io.vertx.scala.core.http.HttpServerResponse
import it.unibo.dcs.commons.JsonHelper.Implicits.jsonArrayToString
import it.unibo.dcs.commons.Logging
import it.unibo.dcs.exceptions.ErrorSubscriber
import it.unibo.dcs.service.webapp.interaction.Results.GetUserParticipationsResult
import it.unibo.dcs.service.webapp.interaction.Results.Implicits.getUserParticipationsToJsonArray
import rx.lang.scala.Subscriber

final class GetUserParticipationsSubscriber(protected override val response: HttpServerResponse)
  extends Subscriber[GetUserParticipationsResult] with ErrorSubscriber with Logging {

  override def onNext(result: GetUserParticipationsResult): Unit = {
    val json: JsonArray = result
    log.debug(s"Retrieving result: $json")
    response end json
  }

}

object GetUserParticipationsSubscriber {
  def apply(response: HttpServerResponse): GetUserParticipationsSubscriber =
    new GetUserParticipationsSubscriber(response)
}

