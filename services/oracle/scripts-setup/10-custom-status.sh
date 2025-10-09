#!/bin/bash

IMPORTED_COMPLETE_FILE=/tmp/import_status/end_epoch.log

ret_code=5
if [ -f $IMPORTED_COMPLETE_FILE ]; then
  bash /opt/oracle/checkDBStatus.sh
  ret_code=$?
fi

exit $ret_code