
import org.apache.spark.sql.streaming.DataStreamWriter
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.jakubf.training.processor.BookRatingProcessor
import org.jakubf.training.sink.StreamSink
import org.jakubf.training.source.StreamSource
import org.scalatest.concurrent.Eventually
import org.scalatest.{FlatSpec, GivenWhenThen, Matchers}

class BookRatingProcessorSpec extends FlatSpec
  with GivenWhenThen with Matchers with Eventually {
    val sparkSession = SparkSession
        .builder()
        .appName("Spark-Hive")
        .config("spark.master", "local")
        .getOrCreate()

    it should "produce correct results" in {
      Given("A Book Rating Processor")
      val bookProducer = new TestBookRatingProcessor(sparkSession)

      When("Passing data through")
      bookProducer.startProcessing

      Then("Should correctly process data")
      import sparkSession.implicits._
      val expectedDF = Seq(
        ("345256689195", "Snow Crash", "2000", 9)
      ).toDF("isbn", "title", "year", "rating")
      val resultDF = bookProducer.streamingSink.asInstanceOf[TestStreamSink].resultDataFrame

      eventually {
        resultDF should not be sparkSession.emptyDataFrame
        resultDF equals expectedDF
      }
    }

  class TestBookRatingProcessor(val sparkSession: SparkSession) extends BookRatingProcessor(sparkSession) {
    override def prepareStreamingSource(): StreamSource = new TestStreamSource()
    override def prepareStreamingSink(): StreamSink = new TestStreamSink()
    override def startAndAwaitTermination(dataFrame: DataStreamWriter[Row]): Unit = {}
    override def fetchExistingData(): DataFrame = {
      import sparkSession.implicits._
      Seq(
        ("345256689195", "9")
      ).toDF("isbn", "rating")
    }
  }

  class TestStreamSource extends StreamSource {
    override def read(): DataFrame = {
      import sparkSession.implicits._
      Seq(
        ("<?xml version=\"1.0\" encoding=\"utf-8\" ?><book year=\"2000\"><title>Snow Crash</title><author>Neal Stephenson</author><publisher>Spectra</publisher><isbn>345256689195</isbn><price>14.95</price></book>")
      ).toDF("value")
    }
  }

  class TestStreamSink() extends StreamSink {
    var resultDataFrame: DataFrame = sparkSession.emptyDataFrame
    override def write(dataFrame: DataFrame): DataStreamWriter[Row] = {
      resultDataFrame = dataFrame
      null
    }
  }
}
