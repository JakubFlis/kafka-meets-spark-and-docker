#!/bin/bash

echo "Running Spark application"

spark-submit \
    --master local[4] \
    --class Main \
    --repositories "https://raw.github.com/banzaicloud/spark-metrics/master/maven-repo/releases" \
    --conf "spark.sql.shuffle.partitions=4" \
    --conf "spark.default.parallelism=4" \
    --driver-memory 2g \
/data/job/transform-assembly.jar