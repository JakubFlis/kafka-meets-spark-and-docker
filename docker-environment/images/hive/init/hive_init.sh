#!/bin/bash

echo "Hive data set up started..."
echo "Create and populate Hive tables"

hive -f /root/hive/init/create_tables.sql

for input_data_dir in $(find /root/hive/init/data_init_files/rating_portal -mindepth 1 -maxdepth 1 -type d); do
    targetDir="$(basename $input_data_dir)"
    mkdir -p /data/hive/warehouse/rating_portal.db/${targetDir,,}
    cp -r $input_data_dir/* /data/hive/warehouse/rating_portal.db/${targetDir,,}/
done

echo "Hive data set up finished."
