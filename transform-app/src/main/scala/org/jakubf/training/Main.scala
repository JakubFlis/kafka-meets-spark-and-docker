package org.jakubf.training

import org.apache.spark.sql.SparkSession
import org.jakubf.training.processor.BookRatingProcessor

object Main extends App {
  override def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("Spark-Hive")
      .config("spark.master", "local")
      .config("hive.metastore.uris", "thrift://172.17.0.1:53001")
      .config("hive.metastore.client.socket.timeout", "300")
      .config("hive.metastore.warehouse.dir", "/data/hive/warehouse")
      .config("javax.jdo.option.ConnectionURL",
        "jdbc:derby:;databaseName=/data/hive/metastore_db;create=true")
      .config("hive.enable.spark.execution.engine", "false")
      .config("hive.exec.stagingdir", "/tmp/hive/hive-staging")
      .enableHiveSupport()
      .getOrCreate()

    new BookRatingProcessor(spark)
      .startProcessing
  }

//  def sparkStreamingKafkaConnectionTest(): Unit = {
//    val spark = SparkSession
//      .builder()
//      .appName("Spark-Hive")
//      .config("spark.master", "local")
//      .config("hive.metastore.uris", "thrift://172.17.0.1:53001")
//      .config("hive.metastore.client.socket.timeout", "300")
//      .config("hive.metastore.warehouse.dir", "/data/hive/warehouse")
//      .config("javax.jdo.option.ConnectionURL",
//        "jdbc:derby:;databaseName=/data/hive/metastore_db;create=true")
//      .config("hive.enable.spark.execution.engine", "false")
//      .config("hive.exec.stagingdir", "/tmp/hive/hive-staging")
//      .enableHiveSupport()
//      .getOrCreate()
//
//    import spark.implicits._
//    import spark.sql
//
//    sql("USE rating_portal")
//    val existingData = sql("SELECT * FROM book_ratings")
//
//    spark
//      .readStream
//      .format("kafka")
//      .option("kafka.bootstrap.servers", "host.docker.internal:9092")
//      .option("subscribe", "foo")
//      .load()
//      .select($"value" cast "string")
//      .withColumn("isbn", $"value")
//      .join(existingData, Seq("isbn"))
//      .writeStream
//      .format("console")
//      .start
//      .awaitTermination()
//  }
}
