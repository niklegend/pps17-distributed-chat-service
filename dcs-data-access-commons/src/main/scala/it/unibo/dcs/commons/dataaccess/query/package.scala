package it.unibo.dcs.commons.dataaccess

import it.unibo.dcs.commons.dataaccess.InsertParams.escape

package object query {

  final class FromBuilder private[query](private[this] val sql: String) {

    def delete: String = "DELETE " + sql

    def select: String = select("*")

    def select(first: String, more: String*): String = queryToString(first :: List(more).flatten, "SELECT") + sql

  }

  final class WhereBuilder private[query](private[this] val sql: String) {

    private[this] val sb: StringBuilder = StringBuilder(sql)

    private[this] var dirty = false

    def select: String = select("*")

    def select(first: String, more: String*): String = queryToString(first :: List(more).flatten, "SELECT") + sql

    private[this] def append(s: String): WhereBuilder = {
      if (!dirty) {
        dirty = true
        sb.append("WHERE ")
        sb.append(s)
      } else {
        sb.append(", ")
        sb.append(s)
      }
      this
    }
  }

  def from(first: String, more: String*): FromBuilder = new FromBuilder(queryToString(first :: List(more).flatten, "FROM"))

  private def queryToString(s: Seq[String], prefix: String): String = s.map(escape).mkString(s"$prefix ", ", ", "")

}
