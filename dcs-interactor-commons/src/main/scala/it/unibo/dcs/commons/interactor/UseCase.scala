package it.unibo.dcs.commons.interactor

import it.unibo.dcs.commons.interactor.executor.Implicits._
import it.unibo.dcs.commons.interactor.executor.{PostExecutionThread, ThreadExecutor}
import rx.lang.scala.{Observable, Subscriber}

abstract class UseCase[T, P](private[this] val threadExecutor: ThreadExecutor,
                             private[this] val postExecutionThread: PostExecutionThread)
  extends Interactor[T, P] {

  override final def apply(parameters: P, observer: Subscriber[T]): Unit = createObservable(parameters)
    .subscribeOn(threadExecutor)
    .observeOn(postExecutionThread)
    .subscribe(observer)

  override final def apply(parameters: P): Observable[T] = createObservable(parameters)

  protected[this] def createObservable(parameters: P): Observable[T]

}
