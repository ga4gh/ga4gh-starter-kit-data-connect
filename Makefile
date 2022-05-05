DOCKER_ORG := ga4gh
DOCKER_REPO := ga4gh-starter-kit-data-connect
DOCKER_TAG := $(shell cat build.gradle | grep "^version" | cut -f 2 -d ' ' | sed "s/'//g")
DOCKER_IMG := ${DOCKER_ORG}/${DOCKER_REPO}:${DOCKER_TAG}
DEVDB := ga4gh-starter-kit.dev.db

.PHONY: docker-build-test
docker-build-test:
	docker build -t ${DOCKER_ORG}/${DOCKER_REPO}:test --build-arg VERSION=${DOCKER_TAG} .

.PHONY: docker-build
docker-build:
	docker build -t ${DOCKER_IMG} --build-arg VERSION=${DOCKER_TAG} .

.PHONY: docker-publish
docker-publish:
	docker image push ${DOCKER_IMG}

# remove local dev db
.PHONY: clean-sqlite
clean-sqlite:
	@rm -f ${DEVDB}

# create the sqlite database
.PHONY: sqlite-db-build
sqlite-db-build: clean-sqlite
	@sqlite3 ${DEVDB} < database/sqlite/create-tables.sql

# populate the sqlite database with test data
.PHONY: sqlite-db-populate-dev-dataset
sqlite-db-populate-dev-dataset:
	@sqlite3 ${DEVDB} < database/sqlite/add-dev-dataset.sql

.PHONY: sqlite-db-refresh
sqlite-db-refresh: clean-sqlite sqlite-db-build sqlite-db-populate-dev-dataset

.PHONY: clean-psql
clean-psql:
	psql -c "drop database starter_kit_db;" -U postgres

.PHONY: psql-db-build
psql-db-build:
	psql -c "create database starter_kit_db;" -U postgres
	psql starter_kit_db < database/postgres/one_thousand_genomes_sample/create-tables.sql -U postgres

.PHONY: psql-db-populate
psql-db-populate:
	psql starter_kit_db < database/postgres/one_thousand_genomes_sample/add-dev-dataset.sql -U postgres

.PHONY: psql-db-build-populate
psql-db-build-populate: psql-db-build psql-db-populate

.PHONY: psql-db-refresh
psql-db-refresh: clean-psql psql-db-build-populate