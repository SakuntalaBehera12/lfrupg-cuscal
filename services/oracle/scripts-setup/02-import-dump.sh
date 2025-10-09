#!/bin/bash

STATUS_DIR=/tmp/import_status
LOG_FILE=/opt/oracle/import/02-import-dump-debug.log

exec >> $LOG_FILE 2>&1

echo "[$(date)] Starting import script..."

mkdir -p "$STATUS_DIR"
date +%s > "$STATUS_DIR/start_epoch.log"

export ORACLE_SID=ORCLCDB

echo "[$(date)] Running impdp..."

impdp liferay/liferay123@ORCLPDB1 \
  DIRECTORY=MY_IMPORT_DIR \
  DUMPFILE=95187_LIFETEST_backup.dmp \
  LOGFILE=import_lifetest.log \
  EXCLUDE=STATISTICS

RESULT=$?

# Treat exit codes 0 and 5 as success
if [[ $RESULT -eq 0 || $RESULT -eq 5 ]]; then
  echo "[$(date)] Import completed with acceptable result (code $RESULT)."
  date +%s > "$STATUS_DIR/end_epoch.log"
  exit 0
else
  echo "[$(date)] Import failed with exit code $RESULT"
  exit $RESULT
fi