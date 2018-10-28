package it.unibo.dcs.commons.validation

import rx.lang.scala.Observable

import it.unibo.dcs.commons.RxHelper.asUnit

private[validation] final class ValidatorImpl[T](private[this] val rules: List[Rule[T]]) extends Validator[T] {

  override def validate(t: T): Observable[Unit] = Observable.from(rules)
    .flatMap(_(t))
    .toList
    .map(asUnit)

}
