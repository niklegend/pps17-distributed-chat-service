package it.unibo.dcs.service.webapp.verticles.handler.impl.subscribers

import it.unibo.dcs.commons.Publisher
import it.unibo.dcs.service.webapp.interaction.Results.NotifyWritingUserResult
import rx.lang.scala.Subscriber

final class NotifyWritingInRoomSubscriber (private[this] val publisher: Publisher)
  extends Subscriber[NotifyWritingUserResult] {

  override def onNext(result: NotifyWritingUserResult): Unit = {
    publisher.publish(result.username)
  }

}

object NotifyWritingInRoomSubscriber {
  def apply(publisher: Publisher): NotifyWritingInRoomSubscriber =
    new NotifyWritingInRoomSubscriber(publisher)
}
