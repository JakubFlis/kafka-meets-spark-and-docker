# Kafka meets Spark and Docker - Windows version


A set of Docker images and application to support an ETL process, which listens for XML values containing book metadata, then joins the metadata with existing, static data, and finally saves the output in the sink database. 
Static data is available in the Hive database under `rating_portal.book_ratings` table, and the output is available in PostgreSQL database under `sink.books` table.

## Structure

![](./img/diagram.png =250x)

### Data Producer App
Data Producer application produces infinitely 10 messages for given Kafka topic every 5 seconds. The messages have following structure:
```
<book year="{YEAR}">
	<title>{TITLE}</title>
	<author>{AUTHOR}</author>
	<publisher>{PUBLISHER}</publisher>
	<isbn>{ISBN}</isbn>
	<price>{PRICE}</price>
</book>
```
Messages are also printed to standard console output.

### Transform App
Transform application listens for Kafka messages sent by the Data Producer application and makes proper operations on received data. Transform app can be easily expanded with more *Sources* and *Sinks*. It only takes to override proper methods of the *Processor* abstract class. 

### Environment

Docker files included under `docker-environment` consist of all necessary dependencies needed for the ETL flow to be successful. Used services:
* Kafka
* Zookeeper
* Hive
* PostgreSQL (NOT used in Windows version - ConsoleStreamSink is used instead due to PostgreSQL Docker image issues)

## Prerequisites

* Docker
* SBT

## Running on Windows

Please run following instructions using separate Terminal windows for each step.

1. Follow these instructions and install Hadoop winutils: https://stackoverflow.com/a/35652866
2. Add an entry to `/etc/hosts` file: `host.docker.internal` with your machine's IP (for Windows - localhost), for example:
`127.0.0.1 host.docker.internal`
3. Go to `transform-app` folder and build Transform Docker image using `sbt buildDockerImage` command
4. Go to `docker-environment` folder and run the environemnt by using `docker-compose up` command
5. Run the Transform app using this command: `docker run -it -v  C:\Users\{USER}\Documents\kafka-meets-spark-and-docker\docker-environment\services\hive\volumes\data:/data/hive jf_transform:latest`. Prior to running the command, you should modify the Hive data path to point to proper directory on your disc.
6. Run the Producer app using this command: `docker run jf_data_producer`
7. Watch batches of data being printed in both terminals, like below:

![](./img/win_screenshot.png =250x)

## TODOs

In order of priority:

* Extract all configuration and properties, make it independent from the app,
* Expand `Process` class to support multiple Sinks and Sources,
* Refactor `build.sbt` to support better versioning,
* Write code documentation,
* Consider CI/CD and Production environment solutions.