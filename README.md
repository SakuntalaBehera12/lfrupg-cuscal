# Liferay dxp-2025.q1.14-lts Portal - Cuscal Project

## Project Overview

The Cuscal project leverages Docker to run a Liferay DXP 2025.q1.14-lts environment integrated with Oracle 19c and Elasticsearch. This setup ensures a consistent, containerized development and runtime workspace.

| Component         | Version              | Docker Image Version  | Notes                                                                                      |
| ----------------- |----------------------|-----------------------|--------------------------------------------------------------------------------------------|
| **Liferay DXP**   | 2025.q1.14-lts       | 2025.q1.14-lts        |                                                                                            |
| **Search Engine** | Elasticsearch 8.17.6 | elasticsearch:8.17.6  |                                                                                            |
| **Java**          | 21                   | (no docker image)     |                                                                                            |
| **Database**      | Oracle 19.19         | enterprise:19.3.0.0   |                                                                                            |
| **Blade CLI**     | 7.0.3.202503050337   | 7.0.3.202503050337    | [Install Blade CLI](https://learn.liferay.com/w/dxp/liferay-development/tooling/blade-cli) |

> **Note**: If you are using Fedora laptop create a Linux VM preferred Ubuntu using Virtual Machine with (10 cores, 20 GB RAM) with Docker, Docker Compose installed.

---

## Services Overview

| Service      | Description                                                  |
| ------------ |--------------------------------------------------------------|
| **database** | Oracle 19c container with schema setup and dump import       |
| **liferay**  | Liferay DXP container, built from liferay/dxp:2025.q1.14-lts |
| **search**   | Elasticsearch container, built from elasticsearch:8.17.6     |

---

## Setup Instructions

~~### Step 1: Prepare Oracle Dump~~

~~1. Download [`95187_LIFETEST_backup.dmp`](https://drive.google.com/file/d/14whQPLUtwJk-OnUtaK0E7YWxFhqJXFtC/view?usp=drive_link) (25.4 GB).~~

~~2. Clone the repo:~~

   ~~```bash~~
   ~~git clone https://github.com/LGSAPAC/lfrgs-cuscal-eft-connect~~
   ~~cd lfrgs-cuscal-eft-connect~~
   ~~git checkout docker-upgrade~~
   ~~```~~

~~3. Move `.dmp` file to the Oracle dump folder:~~

   ~~```bash~~
   ~~mv /path/of/95187_LIFETEST_backup.dmp oracle/dump/~~
   ~~```~~

~~### Step 2: Prepare Oracle Permissions~~

~~1. Ensure permissions~~

~~```bash~~
~~cd lfrgs-cuscal-eft-connect~~
~~chmod 777 -R services/oracle/dump~~
~~sudo chown 54321:54321 services/oracle/dump~~
~~chmod +x services/oracle/scripts-setup/*~~
~~```~~

~~2. Signin to the Oracle Container Registry~~

~~```bash~~
~~docker login container-registry.oracle.com~~
~~```~~
~~

~~If you don't have credentials then signup in the below url and accept their terms and conditions.~~
~~Visit [Oracle Container Registry](https://container-registry.oracle.com), sign in, and accept terms under `Database > enterprise`.~~

### Step 1: Prepare Oracle Database instance

1. Download [`latest-version.dmp`](https://drive.google.com/file/d/1ROeQ1i6VlikuxS3PTSU8Hg-k1XBF3GsW/view?usp=drive_link) (23.6 GB).

2. Follow the instructions in this tutorial: [`How to build a docker image, import and export an Oracle dump using a Docker`](https://liferay.atlassian.net/wiki/spaces/ECU/pages/2157052717/How+to+build+a+docker+image+import+and+export+an+Oracle+dump+using+a+Docker), no need to do the `Creating the docker container` topic.

3. Move `.dmp` file to the Oracle dump folder:

   ```bash
   mv /full-entire-path/[latest-version].dmp [your-cuscal-workspace-directory-full-path]/services/oracle/dump/95187_LIFETEST_backup.dmp
   ```
   ### Note
   >> You need stronger rename the dump name to "95187_LIFETEST_backup.dmp" since this name is expected into the oracle scripts.

### Step 2: Blade CLI Setup

Install Blade CLI:

```bash
curl -L https://raw.githubusercontent.com/liferay/liferay-blade-cli/master/cli/installers/local | sh
vi ~/.bash_profile
# Add:
export PATH="$PATH:$HOME/jpm/bin"
source ~/.bash_profile
```

### Step 3: Build Liferay Workspace

To initialize and create the bundles folder, enter:
```
blade gw initBundle
```

```bash
cd lfrupg-cuscal
blade gw deploy
```

The above command builds and places the wars & jars in the ***bundles/deploy*** and ***bundles/war*** path.

Add the license file to `bundles/deploy/`.

### Step 4: Folder Structure Mapping for information

| Host Directory            | Container Mount Point     |
| ------------------------- | ------------------------- |
| services/liferay/files    | /mnt/liferay/files        |
| bundles/deploy            | /opt/liferay/deploy       |
| bundles/osgi/modules      | /opt/liferay/osgi/modules |
| bundles/logs              | /opt/liferay/logs         |

The Liferay Home folder of Liferay Docker container is mapped to ***services/liferay/files*** path, which contains the portal-ext.properties, osgi/configs, and tomcat folders, so any changes or additions needed are to be made in the respective locations.

---

## Building and Running the Stack

1. Start the stack:

```bash
docker compose up --build -d
```

2. The above command may take approximately 30 minutes to set up all the services Database, Elasticsearch and Liferay from the docker-compose.yml file present within the `lfrgs-cuscal-eft-connect` repository, depending on your system's performance and available resources. Liferay starts last due to its dependencies.

You can monitor the database import process by checking the logs located at **services/oracle/dump/02-import-dump-debug.log**.
Few failing sql queries can be ignored as they are non related to Liferay.

To view live docker-compose logs, open another terminal tab and run:

```bash
docker compose logs -f
```

If you face any issues and all the services don't come up then reset the containers & volume to remove the old data by executing the below commands and then run the same command.

```bash
docker compose down
sudo rm -rf services/oracle/dump/*.log
docker volume rm lfrgs-cuscal-eft-connect_oracle_oradata
docker compose up -d --build
```

---

If you get this in logs then the Database service is ready for use.

```bash
oracle-cuscal  | #########################
oracle-cuscal  | DATABASE IS READY TO USE!
oracle-cuscal  | #########################
```

## Accessing the Stack

* **Liferay**: [http://localhost:8080](http://localhost:8080)

* Oracle DB: Connect via Docker

```bash
docker exec -it oracle-cuscal bash
sqlplus sys/Liferay1237@ORCLCDB as sysdba
ALTER SESSION SET CONTAINER=ORCLPDB1;
```

**Oracle DB**: Connect via DBeaver

* Host: `localhost`
* Port: `1521`
* Service Name: `ORCLPDB1`
* Username: `liferay`
* Password: `Liferay1237`

In case of any query related to users created for liferay look into the script **services/oracle/services/01-database-setup.sql**

---

## Day-to-Day Operations

| Task                          | Command                                     |
| ----------------------------- | ------------------------------------------- |
| Stop all services             | `docker compose stop`                       |
| Stop only Liferay             | `docker compose stop liferay`               |
| View logs (all services)      | `docker compose logs -f`                    |
| Start all services            | `docker compose start`                      |
| View Liferay logs             | `docker compose logs -f liferay`            |
| Shell access to Liferay       | `docker exec -it liferay-cuscal bash`       |
| Shell access to Oracle DB     | `docker exec -it oracle-cuscal bash`        |
| Shell access to Elasticsearch | `docker exec -it elasticsearch-cuscal bash` |

> **Note**: Avoid using `docker compose down` unless resetting a service.

---

## Resetting Services/Containers

### Liferay

```bash
docker compose stop liferay
docker compose rm -f liferay
docker compose up -d --build liferay
```

Please note that before running the docker compose up command, the other services, i.e, DB and ES, must be up and running as liferay is dependent on them.
It can be checked using the command `docker compose ps`

### Oracle Database
1. **Warning:-** All the existing DB data & document & media will be lost after the reset of the database, and a fresh setup with the dump present in the dump folder will be imported.

```bash
docker compose down
sudo rm -rf services/oracle/dump/*.log
docker volume rm lfrgs-cuscal-eft-connect_oracle_oradata
docker compose up -d --build database
```

2. For Windows, run the above steps in the terminal as Administrator.

---

## Patching Instructions

1. If a new patch/Hotfix needs to be installed, then place the new patch in `services/liferay/patching/`
2. Remove the old `.zip`
3. Reset the Liferay container as mentioned above

Logs will indicate patch success:

```
[LIFERAY] Patch applied successfully.
```

---

## Document Library

Cuscal uses DBStore. Document and Media files are included in the Oracle `.dmp` dump.

---

## Liferay Properties
Currently property-files of **.env.uat1** which is at location **lfrgs-cuscal-eft-connect/configs/.env.uat1** path is being used, and we have placed the properties in the **lfrgs-cuscal-eft-connect/services/liferay/files/tomcat/lib** and **lfrgs-cuscal-eft-connect/services/liferay/files/** folder to bind with the docker container. Hence, if you need to update or add anything related to configurations, please do it in **lfrgs-cuscal-eft-connect/services/liferay/files** path and reset the liferay service. To get reflected immediately.

## Known Issues
If you face any errors in liferay related to the below logs in linux.
```bash
liferay-cuscal        | 2025-05-19 12:24:41.140 ERROR [com.liferay.portal.kernel.deploy.auto.AutoDeployScanner][AutoDeployDir:253] Unable to write cuscal-theme.war
liferay-cuscal        | 2025-05-19 12:24:41.141 ERROR [com.liferay.portal.kernel.deploy.auto.AutoDeployScanner][AutoDeployDir:253] Unable to write switch-portlet.war
liferay-cuscal        | 2025-05-19 12:24:41.141 ERROR [com.liferay.portal.kernel.deploy.auto.AutoDeployScanner][AutoDeployDir:253] Unable to write card-management-portlet.war
liferay-cuscal        | 2025-05-19 12:24:41.141 ERROR [com.liferay.portal.kernel.deploy.auto.AutoDeployScanner][AutoDeployDir:253] Unable to write chargeback-portlet.war
```
**Solution:**
Add a parameters in the liferay service of the docker-compose.yml file
**user: root** under the line **image:** as shown in the below sample
```bash
   liferay:
    container_name: liferay-cuscal
    image: liferay/dxp:2025.q1.14-lts
    user: root
    environment:
```
And reset the liferay service.
