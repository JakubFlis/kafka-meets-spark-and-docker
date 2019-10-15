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

## Running the process

To run the process:
1. Execute `bash buildEnvsAndStartTransform.sh`
2. In another terminal window, execute `startProducer.sh`

In order to check the output data in the DB, run
`bash -c "clear && docker exec -it postgres sh"`
and fetch records from `sink.books` table.

## TODOs
In order of priority:
- Write unit tests for all Processors,
- Extract all configuration and properties, make it independent from the app,
- Expand `Process` class to support multiple Sinks and Sources,
- Refactor `build.sbt` to support better versioning,
- Code documentation,
- Consider CI/CD solutions.