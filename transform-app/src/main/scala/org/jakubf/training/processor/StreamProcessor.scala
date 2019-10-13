package org.jakubf.training.processor

import org.apache.spark.sql.streaming.DataStreamWriter
import org.apache.spark.sql.Row
import org.jakubf.training.sink.StreamSink
import org.jakubf.training.source.StreamSource

abstract class StreamProcessor() {
    def startProcessing(): Unit

    def startAndAwaitTermination(dataFrame: DataStreamWriter[Row]): Unit = {
      dataFrame
        .start
        .awaitTermination()
    }

    def prepareStreamingSource(): StreamSource

    def prepareStreamingSink(): StreamSink
}
