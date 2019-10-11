#!/bin/bash
echo "Booting Hive docker. That can take around 1 minute."

if [ ! -d "/data/hive/metastore_db" ]; then
  cd /data/hive
  $HIVE_HOME/bin/schematool -dbType derby -initSchema
  cd
fi

$HIVE_HOME/hcatalog/sbin/hcat_server.sh stop
$HIVE_HOME/hcatalog/sbin/hcat_server.sh start

/root/hive/init/hive_init.sh

/usr/bin/tail -f /dev/null