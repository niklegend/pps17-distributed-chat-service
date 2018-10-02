package it.unibo.dcs.commons.service

import io.vertx.scala.ext.web.client.WebClient
import rx.lang.scala.Observable

trait HttpEndpointDiscovery {

  def getWebClient(name: String): Observable[WebClient]

  def getWebClientOrFail(name: String): Observable[WebClient]

}
