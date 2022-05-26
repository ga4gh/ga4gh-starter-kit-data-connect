<img src="https://www.ga4gh.org/wp-content/themes/ga4gh-theme/gfx/GA-logo-horizontal-tag-RGB.svg" alt="GA4GH Logo" style="width: 400px;"/>

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](https://opensource.org/licenses/Apache-2.0)
[![Java 11+](https://img.shields.io/badge/java-11+-blue.svg?style=flat-square)](https://www.java.com)
[![Gradle 7.3.3+](https://img.shields.io/badge/gradle-7.3.3+-blue.svg?style=flat-square)](https://gradle.org/)
[![GitHub Actions](https://img.shields.io/github/workflow/status/ga4gh/ga4gh-starter-kit-data-connect/test/main)](https://github.com/ga4gh/ga4gh-starter-kit-data-connect/actions)
[![Codecov](https://img.shields.io/codecov/c/github/ga4gh/ga4gh-starter-kit-data-connect?style=flat-square)](https://app.codecov.io/gh/ga4gh/ga4gh-starter-kit-data-connect)

# GA4GH Starter Kit Data Connect

Starter Kit server implementation of the GA4GH [Data Connect API specification](https://github.com/ga4gh-discovery/data-connect).

Please see [starterkit.ga4gh.org](https://starterkit.ga4gh.org) for the full documentation on how to run the Starter Kit, including Data Connect.

Basic instructions for running Starter Kit Data Connect in a dev environment are included here. 

## Run Data Connect

### Docker

We recommend running Data Connect as a docker container for most contexts. Images can be downloaded from [docker hub](https://hub.docker.com/repository/docker/ga4gh/ga4gh-starter-kit-data-connect). To download the image and run a container:

Pull the image:
```
docker pull ga4gh/ga4gh-starter-kit-data-connect:latest
```

Run container with default settings:
```
docker run -p 4500:4500 ga4gh/ga4gh-starter-kit-data-connect:latest
```

OR, run container with config file overriding defaults
```
docker run -p 4500:4500 ga4gh/ga4gh-starter-kit-data-connect:latest java -jar ga4gh-starter-kit-data-connect.jar -c path/to/config.yml
```
### Native (on local OS)

### Native

The service can also be installed locally in cases where docker deployments are not possible, or for development of the codebase. Native installations require:
* Java 11+
* Gradle 7.3.2+
* SQLite (for creating the dev database)

First, clone the repository from Github:
```
git clone https://github.com/ga4gh/ga4gh-starter-kit-data-connect.git
cd ga4gh-starter-kit-data-connect
```

The service can be run in development mode directly via gradle:

Run with all defaults
```
./gradlew bootRun
```

Run with config file
```
./gradlew bootRun --args="--config path/to/config.yml"
```

Alternatively, the service can be built as a jar and run:

Build jar:
```
./gradlew bootJar
```

Run with all defaults
```
java -jar build/libs/ga4gh-starter-kit-data-connect-${VERSION}.jar
```

Run with config file
```
java -jar build/libs/ga4gh-starter-kit-data-connect-${VERSION}.jar --config path/to/config.yml
```

### Confirm server is running

Whether running via docker or natively on a local machine, confirm the Data Connect service is up & running by visiting its `service-info` endpoint, you should receive a valid `ServiceInfo` response.

```
GET http://localhost:4500/service-info

Response:
{
    "id": "org.ga4gh.starterkit.dataconnect",
    "name": "GA4GH Starter Kit Data Connect Service",
    "description": "Starter Kit implementation of the  Data Connect API specification. Gives researchers access to the data  model of given datasets/tables, and enables them to perform search  queries on the datasets using the model.",
    "contactUrl": "mailto:info@ga4gh.org",
    "documentationUrl": "https://github.com/ga4gh/ga4gh-starter-kit-data-connect",
    "createdAt": "2022-04-27T09:00:00Z",
    "updatedAt": "2022-04-27T09:00:00Z",
    "environment": "test",
    "version": "0.1.0",
    "type": {
        "group": "org.ga4gh",
        "artifact": "data-connect",
        "version": "1.0.0"
    },
    "organization": {
        "name": "Global Alliance for Genomics and Health",
        "url": "https://ga4gh.org"
    }
}
```
## Development

Additional setup steps to run the Data Connect server in a local environment for development and testing.

### Setup dev database

A local SQLite database must be set up for running the Data Connect service in a development context. If `make` and `sqlite3` are already installed on the system `PATH`, this database can be created and populated with a dev dataset by simply running:

```
make sqlite-db-refresh
```

This will create a SQLite database named `ga4gh-starter-kit.dev.db` in the current directory.

If the above command fails, check if `make` and `sqlite` are installed, [this file](./database/sqlite/create-tables.sql) contains SQLite commands for creating the database schema, and [this file](./database/sqlite/add-dev-dataset.sql) contains SQLite commands for populating it with the dev dataset.

Confirm the Data Connect service can connect to the dev database by sending a GET request to `/tables` endpoint.

```
GET http://localhost:4500/tables

Response:
{
    "tables": [
        {
            "name": "one_thousand_genomes_sample",
            "description": "Table / directory containing JSON files for one thousand genomes sample from https://www.internationalgenome.org",
            "data_model": {
                "$ref": "http://localhost:4500/table/one_thousand_genomes_sample/info"
            }
        },
        {
            "name": "phenopacket_v1",
            "description": "Table / directory containing JSON files for phenopackets",
            "data_model": {
                "$ref": "http://localhost:4500/table/phenopacket_v1/info"
            }
        }
    ]
}
```
**NOTE:** If running via docker, the dev database is already bundled within the container.

**NOTE:** The unit and end-to-end test suite is predicated on a preconfigured database. The SQLite dev database must be present for tests to pass.

## Dev datasets

The SQLite dev database is preconfigured with two datasets to aid development and testing. The following datasets are included:
* A subset of 200 genome samples from the one thousand genome samples dataset: [Paper](https://www.nature.com/articles/nature15393) , [Dataset homepage](https://www.internationalgenome.org/data)
* A subset of 50 phenopackets from an open dataset of 384 Phenopackets: [Paper](https://pubmed.ncbi.nlm.nih.gov/32755546/), [Dataset homepage](https://zenodo.org/record/3905420#.YArkBzpKhPZ)