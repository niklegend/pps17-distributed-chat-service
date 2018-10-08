import io.vertx.ext.unit.report.ReportOptions
import io.vertx.ext.unit.{TestOptions, TestSuite}
import io.vertx.lang.scala.ScalaVerticle.nameForVerticle
import io.vertx.scala.core.Vertx
import it.unibo.dcs.service.webapp.verticles.WebAppVerticle
import org.scalamock.matchers.Matchers
import org.scalamock.scalatest.MockFactory

import scala.language.postfixOps

object WebappServiceSuite extends App with MockFactory with Matchers {

  val vertx = Vertx vertx()
  val testSuite: TestSuite = TestSuite.create("WebappService Test Suite")
    .test("WebApp Service is successfully deployed", context => {
      //val async = context.async()
      vertx.deployVerticle(nameForVerticle[WebAppVerticle], context.asyncAssertSuccess[String]())
      //async.awaitSuccess()
    })

  testSuite.run(new TestOptions().addReporter(new ReportOptions().setTo("console")))
}
