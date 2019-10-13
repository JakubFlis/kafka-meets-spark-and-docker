package org.jakubf.training.source

import org.apache.spark.sql.streaming.DataStreamReader
import org.apache.spark.sql.{DataFrame, SparkSession}

class KafkaStreamSource(dataStreamReader: DataStreamReader) extends StreamSource {
  override def read(): DataFrame = {
    dataStreamReader
      .format("kafka")
      .option("kafka.bootstrap.servers", "host.docker.internal:9092")
      .option("subscribe", "foo")
      .load()
  }
}