#!/bin/bash

echo "Create and populate Hive tables"

hive -f /root/hive/init/create_tables.sql

mkdir -p /data/hive/warehouse/rating_portal.db/book_ratings
cp -r $input_data_dir/* /data/hive/warehouse/rating_portal.db/book_ratings/

echo "Hive data set up finished."
