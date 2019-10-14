package org.jakubf.training.sink

import java.util.Properties

import org.apache.spark.sql.streaming.DataStreamWriter
import org.apache.spark.sql.{DataFrame, Row, SaveMode}

class PostgresStreamSink extends StreamSink {
  override def write(dataFrame: DataFrame): DataStreamWriter[Row] = {
    val database = "book_sink"
    val url: String = s"jdbc:postgresql://172.17.0.1/$database"
    val tableName: String = "books"
    val user: String = "postgres"
    val password: String = "changeme"

    val properties = new Properties()
    properties.setProperty("user", user)
    properties.setProperty("password", password)
    properties.put("driver", "org.postgresql.Driver")

    val redirectToPostgresSink = { (dataFrame: DataFrame, _: Long) ⇒
      dataFrame.show
      dataFrame
        .write
        .mode(SaveMode.Append)
        .jdbc(url, tableName, properties)
    }

    dataFrame
      .writeStream
      .foreachBatch(redirectToPostgresSink)
  }
}