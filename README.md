# Kafka meets Spark and Docker

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

Docker files included under `docker-environment`consist of all necessary dependencies needed for the ETL flow to be successful. Used services:
* Kafka
* Zookeeper
* Hive
* PostgreSQL

## Prerequisites

* Docker
* SBT
* Windows users: https://stackoverflow.com/a/35652866

## Running the process using scripts

To run the process (tested on macOS):

1. Modify path in the `run_transform_app_docker_image` function inside `buildEnvsAndStartTransform.sh` script to properly point to `docker-environment/services/hive/volumes/data` directory
1. Add an entry to `/etc/hosts` file: `host.docker.internal` with your machine's IP, for example:
`177.16.19.206 host.docker.internal`
2. Execute `bash buildEnvsAndStartTransform.sh`
3. In another terminal window, execute `startProducer.sh`
4. You should see generated and consumed data in both terminals.

In order to check the output data in the DB, run
`bash -c "clear && docker exec -it postgres sh"`
and fetch records from `sink.books` table.

## Running on Windows

You can follow the script solution from above, but it's recommended to use these manual steps when running the system on Windows OS:

1. Follow these instructions and install Hadoop winutils: https://stackoverflow.com/a/35652866
2. Add an entry to `/etc/hosts` file: `host.docker.internal` with your machine's IP, for example:
`177.16.19.206 host.docker.internal`
3. Go to `transform-app` folder and build Transform Docker image using `sbt buildDockerImage` command
4. Go to `docker-environment` folder and run the environemnt by using `docker-compose up` command
5. Run the Transform app using this command: `docker run -it -v /Users/jakubflis/Projects/kafka-meets-spark-and-docker/docker-environment/services/hive/volumes/data:/data/hive jf_transform:latest`. Prior to running the command, you should modify the Hive data path to point to proper directory on your disc.
6. Run the Producer app using this command: `docker run jf_data_producer`


## TODOs

In order of priority:

* Extract all configuration and properties, make it independent from the app,
* Expand `Process` class to support multiple Sinks and Sources,
* Refactor `build.sbt` to support better versioning,
* Code documentation,
* Consider CI/CD solutions.