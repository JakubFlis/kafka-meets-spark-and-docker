package org.jakubf.training.processor

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.udf
import org.jakubf.training.sink.{ConsoleStreamSink, StreamSink}
import org.jakubf.training.source.{KafkaStreamSource, StreamSource}

class BookRatingProcessor(spark: SparkSession) extends StreamProcessor {
    override def startProcessing(): Unit = {
        import spark.implicits._
        val extractTitle = udf[String, String] { xml ⇒
            scala.xml.XML.loadString(xml) \ "title" text
        }
        val extractYear = udf[String, String] { xml ⇒
            scala.xml.XML.loadString(xml) \@ "year"
        }
        val extractIsbn = udf[String, String] { xml ⇒
            scala.xml.XML.loadString(xml) \ "isbn" text
        }

        val streamingSource = prepareStreamingSource
        val streamingSink = prepareStreamingSink
        val streamData = streamingSource.read
        val joinedData = join(fetchExistingData, streamData)
          .select($"value" cast "string")
          .withColumn("title", extractTitle($"value"))
          .withColumn("year", extractYear($"value"))
          .withColumn("isbn", extractIsbn($"value"))
          .drop("value")

        val sinkData = streamingSink.write(joinedData)

        startAndAwaitTermination(sinkData)
    }

    def join(existingData: DataFrame, incomingData: DataFrame): DataFrame = {
        existingData.join(incomingData, Seq("isbn"))
    }

    def fetchExistingData(): DataFrame = {
        import spark.sql
        sql("USE rating_portal")
        sql("SELECT * FROM book_ratings")
    }

    override def prepareStreamingSource(): StreamSource =
        new KafkaStreamSource(spark.readStream.format("com.databricks.spark.xml"))

    override def prepareStreamingSink(): StreamSink =
        new ConsoleStreamSink()
}