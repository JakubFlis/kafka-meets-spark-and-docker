CREATE DATABASE IF NOT EXISTS rating_portal;

CREATE TABLE IF NOT EXISTS rating_portal.book_ratings (
    isbn            bigint,
    rating          bigint
) ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
 STORED AS TEXTFILE
 LOCATION 'file:/data/hive/warehouse/rating_portal.db/book_ratings';
