package org.jakubf.training

import java.util.Properties

trait PostgreSQLConfiguration {
  lazy val postgreSqlPassword: String = sys.env.getOrElse("POSTGRESQL_PASSWORD", "changeme")
  lazy val postgreSqlUsername: String = sys.env.getOrElse("POSTGRESQL_USERNAME", "postgres")
  lazy val postgreSqlHost: String = sys.env.getOrElse("POSTGRESQL_HOST", "172.17.0.1")
  lazy val postgreSqlDatabaseName: String = sys.env.getOrElse("POSTGRESQL_DB_NAME", "")
  lazy val postgreSqlTableName: String = sys.env.getOrElse("POSTGRESQL_TABLE_NAME", "")
  lazy val postgreSqlJdbcUrl = s"jdbc:postgresql://$postgreSqlHost/$postgreSqlDatabaseName"

  lazy val postgreSqlProperties: Properties = {
    val properties = new Properties()
    properties.setProperty("user", postgreSqlUsername)
    properties.setProperty("password", postgreSqlPassword)
    properties.put("driver", "org.postgresql.Driver")
    properties
  }
}
