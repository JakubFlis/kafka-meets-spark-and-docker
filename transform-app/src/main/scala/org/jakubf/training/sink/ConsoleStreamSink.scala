package org.jakubf.training.sink
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.streaming.DataStreamWriter

class ConsoleStreamSink extends StreamSink {
  override def write(dataFrame: DataFrame): DataStreamWriter[Row] = {
      dataFrame
        .writeStream
        .format("console")
  }
}
