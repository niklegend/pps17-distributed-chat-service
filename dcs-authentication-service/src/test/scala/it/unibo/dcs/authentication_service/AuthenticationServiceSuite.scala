package it.unibo.dcs.authentication_service

import io.vertx.ext.unit.TestSuite
import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import io.vertx.scala.core.Vertx
import it.unibo.dcs.authentication_service.server.AuthenticationVerticle
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec
import scala.language.postfixOps

class AuthenticationServiceSuite extends FlatSpec with MockFactory {

  val testSuite: TestSuite = TestSuite.create("Authentication Test Suite")
  testSuite.beforeEach(_ =>
    Vertx vertx() deployVerticle nameForVerticle[AuthenticationVerticle])
    .afterEach(_ => {
    Vertx vertx() undeploy Vertx.vertx().deploymentIDs().head
  })

  "AuthenticationService" should "fail if registration request has an empty body" in {
    testSuite.test("empty registration fails", context => {
      val async1 = context.async()
      val client = Vertx vertx() createHttpClient
      val req = client.post(8080, "localhost", "/register")
      req.exceptionHandler(err => context.fail(err.getMessage))
      req.handler(resp => {
        context.assertEquals(500, resp.statusCode)
        async1.complete()
      })
      async1.awaitSuccess()
    }).run().awaitSuccess()
  }
}
