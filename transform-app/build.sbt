import scala.sys.process.Process

name := "transform"
version := "0.1"
scalaVersion := "2.11.12"

val SPARK_VERSION = "2.4.0"
val SCALA_TEST_VERSION = "3.0.8"

libraryDependencies += "org.apache.spark" %% "spark-core" % SPARK_VERSION
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % SPARK_VERSION
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % SPARK_VERSION
libraryDependencies += "org.apache.spark" %% "spark-streaming" % SPARK_VERSION
libraryDependencies += "org.apache.spark" %% "spark-hive" % SPARK_VERSION
libraryDependencies += "com.databricks" %% "spark-xml" % "0.6.0"
libraryDependencies += "org.scalactic" %% "scalactic" % SCALA_TEST_VERSION
libraryDependencies += "org.scalatest" %% "scalatest" % SCALA_TEST_VERSION % "test"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.2.0"
libraryDependencies += "postgresql" % "postgresql" % "9.1-901-1.jdbc4"

val buildDockerImage = taskKey[Unit]("")
buildDockerImage := {
  val imageName = "jf_transform"
  val path = "docker-app"
  Process(
    "docker" :: "build" ::
      "--label" :: imageName ::
      "--file" :: s"${path}/Dockerfile" ::
      "--tag" :: imageName ::
    path :: Nil
  ).!
}

assemblyMergeStrategy in assembly := {
  case "META-INF/services/org.apache.spark.sql.sources.DataSourceRegister" => MergeStrategy.concat
  case "META-INF/MANIFEST.MF" => MergeStrategy.discard
  case PathList("org", "slf4j", xs @ _ *) => MergeStrategy.first
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

assemblyOutputPath in assembly := new File("docker-app/transform-assembly.jar")

buildDockerImage := buildDockerImage
  .dependsOn(assembly)
  .value
