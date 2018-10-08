package it.unibo.dcs.service.room.data

import io.vertx.ext.unit.report.ReportOptions
import io.vertx.ext.unit.{TestOptions, TestSuite}
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.jdbc.JDBCClient
import io.vertx.scala.ext.sql.SQLConnection
import it.unibo.dcs.commons.IoHelper
import it.unibo.dcs.service.room.data.impl.RoomDataStoreDatabase
import it.unibo.dcs.service.room.request.DeleteRoomRequest
import org.scalatest.FlatSpec

final class RoomDataStoreSpec extends FlatSpec {

  private var vertx: Vertx = _
  private var roomDataStore: RoomDataStore = _

  private var client: JDBCClient = _
  private var connection: SQLConnection = _

  it should "" in {
    TestSuite.create("")
      .before(context => {
        vertx = Vertx.vertx
        val config = IoHelper.readJsonObject("/db_config.json")

        client = JDBCClient.createShared(vertx, config)

        client.getConnection(context.asyncAssertSuccess(result => {
            connection = result
            roomDataStore = new RoomDataStoreDatabase(connection)
        }))
      })
      .after(context => {
        connection.close(context.asyncAssertSuccess())
        client.close(context.asyncAssertSuccess())
        vertx.close(context.asyncAssertSuccess())
      })
      .test("Delete a room", context => {
        println("Begin test")

        connection.execute("INSERT INTO `users` (`username`) VALUES ('mvandi');", context.asyncAssertSuccess())

        connection.execute("INSERT INTO `rooms` (`name`, `owner_username`) VALUES ('Test room', 'mvandi');", context.asyncAssertSuccess())

        connection.query("SELECT COUNT(*) FROM rooms", context.asyncAssertSuccess(result => {
            val count = result.getResults.head.getInteger(0)
            context.assertEquals(1, count)
            println("secondCountAsync.complete()")
        }))

        val secondCountAsync = context.async
        roomDataStore.deleteRoom(DeleteRoomRequest("Test room", "mvandi"))
          .subscribe(_ => {
            connection.query("SELECT COUNT(*) FROM rooms", ar => {
              if (ar.succeeded) {
                val count = ar.result.getResults.head.getInteger(0)
                context.assertEquals(0, count)
                println("secondCountAsync.complete()")
                secondCountAsync.complete()
              } else {
                context.fail(ar.cause())
              }
            })
          }, context.fail)
        secondCountAsync.await()

        println("Finished test")
      })
      .run(new TestOptions().addReporter(new ReportOptions().setTo("console")))
  }

}
