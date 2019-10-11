name := "transform"

version := "0.1"

scalaVersion := "2.12.1"

val SPARK_VERSION = "2.4.4"

libraryDependencies += "org.apache.kafka" %% "kafka" % "2.3.0"
libraryDependencies += "org.apache.spark" %% "spark-core" % SPARK_VERSION
libraryDependencies += "org.apache.spark" %% "spark-streaming" % SPARK_VERSION % "provided"


