FROM oracle/database:19.3.0-se2

COPY --chown=54321:54322 services/oracle/dump/ /opt/oracle/import
COPY --chown=54321:54322 services/oracle/scripts-setup/ /opt/oracle/scripts/setup