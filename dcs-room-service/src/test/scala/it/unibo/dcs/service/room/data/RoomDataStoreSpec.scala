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

        val connectionAsync = context.async
        client.getConnection(ar => {
          if (ar.succeeded) {
            connection = ar.result
            roomDataStore = new RoomDataStoreDatabase(connection)
            connectionAsync.complete()
          } else {
            context.fail(ar.cause)
          }
        })
        connectionAsync.await()
      }) /*
      .after(context => {
        connection.close(_ => {
          val closeAsync = context.async
          client.close(ar => {
            vertx.close()
            if (ar.succeeded()) {
              closeAsync.complete()
            } else {
              context.fail(ar.cause)
            }
          })
        })
      })*/
      .test("Delete a room", context => {
      val insertAsync = context.async

      connection.execute("INSERT INTO `users` (`username`) VALUES ('mvandi');", ar => {
        if (ar.succeeded) {
          connection.execute("INSERT INTO `rooms` (`name`, `owner_username`) VALUES ('Test room', 'mvandi');", ar => {
            if (ar.succeeded) {
              connection.query("SELECT COUNT(*) FROM rooms", ar => {
                if (ar.succeeded) {
                  val result = ar.result
                  context.assertEquals(1, result.getResults.size)
                  println("insertAsync.complete()")
                  insertAsync.complete()
                } else {
                  context.fail(ar.cause())
                }
              })
            } else {
              context.fail(ar.cause())
            }
          })
        } else {
          context.fail(ar.cause())
        }
      })

      insertAsync.await()

      val deleteAsync = context.async
      roomDataStore.deleteRoom(DeleteRoomRequest("Test room", "mvandi"))
        .subscribe(_ => {
          connection.query("SELECT COUNT(*) FROM rooms", ar => {
            if (ar.succeeded) {
              val result = ar.result
              context.assertEquals(0, result.getResults.head.getInteger(0))
              println("deleteAsync.complete()")
              deleteAsync.complete()
            } else {
              context.fail(ar.cause())
            }
          })
        }, context.fail)
      deleteAsync.await()
      println("Finished test")
    })
      .run(new TestOptions().addReporter(new ReportOptions().setTo("console")))
  }

}
