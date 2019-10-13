package org.jakubf.training.sink

import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.streaming.DataStreamWriter

trait StreamSink {
  def write(dataFrame: DataFrame): DataStreamWriter[Row]
}
