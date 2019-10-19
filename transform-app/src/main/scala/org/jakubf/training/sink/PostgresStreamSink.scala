package org.jakubf.training.sink

import org.apache.spark.sql.streaming.DataStreamWriter
import org.apache.spark.sql.{DataFrame, Row, SaveMode}
import org.jakubf.training.PostgreSQLConfiguration

class PostgresStreamSink extends StreamSink with PostgreSQLConfiguration {
  override def write(dataFrame: DataFrame): DataStreamWriter[Row] = {
    val redirectToPostgresSink = { (dataFrame: DataFrame, _: Long) ⇒
      dataFrame.show
      dataFrame
        .write
        .mode(SaveMode.Append)
        .jdbc(postgreSqlJdbcUrl, postgreSqlTableName, postgreSqlProperties)
    }

    dataFrame
      .writeStream
      .foreachBatch(redirectToPostgresSink)
  }
}