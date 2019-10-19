package org.jakubf.training

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

trait LocalSparkConfiguration {
  lazy val hiveMetastoreUri = sys.env.getOrElse("HIVE_METASTORE_URI", "thrift://172.17.0.1:53001")
  lazy val hiveWarehouseDir = sys.env.getOrElse("HIVE_WAREHOUSE_DIR", "/data/hive/warehouse")
  lazy val hiveJDBCUrl = sys.env.getOrElse("HIVE_JDBC_URL", "jdbc:derby:;databaseName=/data/hive/metastore_db;create=true")

  private lazy val sparkHiveConf = new SparkConf()
    .set("spark.master", "local")
    .set("hive.metastore.uris", hiveMetastoreUri)
    .set("hive.metastore.warehouse.dir", hiveWarehouseDir)
    .set("javax.jdo.option.ConnectionURL", hiveJDBCUrl)
    .set("hive.metastore.client.socket.timeout", "300")
    .set("hive.enable.spark.execution.engine", "false")
    .set("hive.exec.stagingdir", "/tmp/hive/hive-staging")

  private lazy val sparkConf = new SparkConf()
    .set("spark.master", "local")
    .set("spark.sql.shuffle.partitions", "4")

  implicit lazy val hiveSparkSession: SparkSession = SparkSession.builder
    .config(sparkHiveConf)
    .enableHiveSupport()
    .getOrCreate()

  implicit lazy val sparkSession: SparkSession = SparkSession.builder
    .config(sparkConf)
    .getOrCreate()
}
