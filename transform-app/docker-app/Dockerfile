FROM openjdk:8u92-jdk-alpine

# Versions
ENV SCALA_VERSION 2.11.11
ENV HADOOP_VERSION 2.8.3
ENV SPARK_VERSION 2.4.0
ENV SBT_VERSION 0.13.15

# SBT related variables.
ENV SBT_HOME /usr/local/sbt
ENV SBT_BINARY_ARCHIVE_NAME sbt-$SBT_VERSION
ENV SBT_BINARY_DOWNLOAD_URL https://dl.bintray.com/sbt/native-packages/sbt/${SBT_VERSION}/${SBT_BINARY_ARCHIVE_NAME}.tgz
ENV PATH $SBT_HOME/bin:$PATH

# Scala related variables.
ENV SCALA_HOME /usr/local/scala
ENV SCALA_BINARY_ARCHIVE_NAME scala-${SCALA_VERSION}
ENV SCALA_BINARY_DOWNLOAD_URL http://downloads.lightbend.com/scala/${SCALA_VERSION}/${SCALA_BINARY_ARCHIVE_NAME}.tgz
ENV PATH $SCALA_HOME/bin:$PATH

# Hadoop related variables.
ENV HADOOP_HOME /usr/local/hadoop
ENV HADOOP_CONF_DIR $HADOOP_HOME/conf
ENV HADOOP_BINARY_ARCHIVE_NAME hadoop-$HADOOP_VERSION
ENV HADOOP_BINARY_DOWNLOAD_URL https://archive.apache.org/dist/hadoop/common/hadoop-$HADOOP_VERSION/hadoop-$HADOOP_VERSION.tar.gz
ENV PATH $HADOOP_HOME/bin:$PATH

# Spark related variables.
ENV SPARK_HOME /usr/local/spark
ENV SPARK_CONF_DIR $SPARK_HOME/conf
ENV SPARK_BINARY_ARCHIVE_NAME spark-$SPARK_VERSION-bin-hadoop2.7
ENV SPARK_BINARY_DOWNLOAD_URL https://archive.apache.org/dist/spark/spark-$SPARK_VERSION/spark-$SPARK_VERSION-bin-hadoop2.7.tgz
ENV PATH $SPARK_HOME/bin:$PATH

# Add auxiliary libs
RUN apk add -U curl bash python3 openssl procps snappy

# Install SBT
RUN wget -qO - ${SBT_BINARY_DOWNLOAD_URL} | tar -xz -C /usr/local/
RUN ln -s /usr/local/${SBT_BINARY_ARCHIVE_NAME} ${SBT_HOME}

# Install Scala
RUN wget -qO - ${SCALA_BINARY_DOWNLOAD_URL} | tar -xz -C /usr/local/
RUN ln -s /usr/local/${SCALA_BINARY_ARCHIVE_NAME} ${SCALA_HOME}

# Install Hadoop
RUN wget -qO - ${HADOOP_BINARY_DOWNLOAD_URL} | tar -xz -C /usr/local/
RUN ln -s /usr/local/${HADOOP_BINARY_ARCHIVE_NAME} ${HADOOP_HOME}
RUN mkdir -p $HADOOP_HOME/logs \
  && mkdir -p $HADOOP_CONF_DIR \
  && chmod 777 $HADOOP_CONF_DIR \
  && chmod 777 $HADOOP_HOME/logs
ADD hive-site.xml ${HADOOP_CONF_DIR}

# Install Spark
RUN wget -qO - ${SPARK_BINARY_DOWNLOAD_URL} | tar -xz -C /usr/local/
RUN ln -s /usr/local/${SPARK_BINARY_ARCHIVE_NAME} ${SPARK_HOME}
RUN mkdir -p /data/spark/ \
  && mkdir -p $SPARK_HOME/logs \
  && mkdir -p $SPARK_CONF_DIR \
  && chmod 777 $SPARK_HOME/logs
RUN cp ${SPARK_CONF_DIR}/log4j.properties.template ${SPARK_CONF_DIR}/log4j.properties && \
    sed -i -e s/WARN/ERROR/g ${SPARK_CONF_DIR}/log4j.properties && \
    sed -i -e s/INFO/ERROR/g ${SPARK_CONF_DIR}/log4j.properties
ADD hive-site.xml ${SPARK_CONF_DIR}

RUN mkdir -p /data
RUN mkdir -p /data/config
RUN mkdir -p /data/schemas
RUN mkdir -p /data/job
RUN mkdir -p /data/hive

COPY . /data/job

USER root
WORKDIR /root

EXPOSE 4040 8080 8081

RUN ["chmod", "+x", "/data/job/entrypoint.sh"]
ENTRYPOINT ["/data/job/entrypoint.sh"]
