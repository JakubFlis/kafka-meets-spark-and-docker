CREATE DATABASE IF NOT EXISTS rating_portal;

CREATE EXTERNAL TABLE IF NOT EXISTS `rating_portal.book_ratings` (
    `isbn`            bigint,
    `rating`          bigint
) ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
 STORED AS TEXTFILE
 LOCATION '/data/hive/warehouse/rating_portal.db/book_ratings';
