package it.unibo.dcs.service.room.data

import io.vertx.ext.unit.TestSuite
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.jdbc.JDBCClient
import io.vertx.scala.ext.sql.{ResultSet, SQLConnection}
import it.unibo.dcs.commons.VertxHelper
import it.unibo.dcs.service.room.data.impl.RoomDataStoreDatabase
import it.unibo.dcs.service.room.request.DeleteRoomRequest
import org.scalatest.FlatSpec

import scala.io.Source

final class RoomDataStoreSpec extends FlatSpec {

  private var roomDataStore: RoomDataStore = _

  private var client: JDBCClient = _
  private var connection: SQLConnection = _

  it should "" in {
    TestSuite.create("")
      .beforeEach(context => {
        val vertx = Vertx.vertx
        val config = VertxHelper.readJsonObject("/test_db_config.json")

        client = JDBCClient.createNonShared(vertx, config)

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

        val executeAsync = context.async
        connection.execute(Source.fromFile("/RoomService.sql", "UTF-8").toString, ar => {
          if (ar.succeeded) {
            executeAsync.complete()
          } else {
            context.fail(ar.cause)
          }
        })
        executeAsync.await()
      })
      .afterEach(context => {
        connection.close(_ => {
          val closeAsync = context.async
          client.close(ar => {
            if (ar.succeeded()) {
              closeAsync.complete()
            } else {
              context.fail(ar.cause)
            }
          })
        })
      })
      .test("Delete a room", context => {
        val insertAsync = context.async

        connection.execute("INSERT INTO users(username) VALUES('mvandi'); INSERT INTO rooms(name, owner_name) VALUES('Test room', 'mvandi')", ar => {
          if (ar.succeeded) {
            connection.query("SELECT COUNT(*) FROM rooms", ar => {
              if (ar.succeeded) {
                val result = ar.result
                context.assertEquals(1, result.getResults.size)
                insertAsync.complete()
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
                context.assertEquals(0, result.getResults.size)
                deleteAsync.complete()
              } else {
                context.fail(ar.cause())
              }
            })
          }, context.fail)
        deleteAsync.await()
      })
      .run()
  }

}
