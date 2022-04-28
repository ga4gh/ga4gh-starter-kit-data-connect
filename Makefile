DOCKER_ORG := ga4gh
DOCKER_REPO := ga4gh-starter-kit-data-connect
DOCKER_TAG := $(shell cat build.gradle | grep "^version" | cut -f 2 -d ' ' | sed "s/'//g")
DOCKER_IMG := ${DOCKER_ORG}/${DOCKER_REPO}:${DOCKER_TAG}

.PHONY: docker-build-test
docker-build-test:
	docker build -t ${DOCKER_ORG}/${DOCKER_REPO}:test --build-arg VERSION=${DOCKER_TAG} .

.PHONY: docker-build
docker-build:
	docker build -t ${DOCKER_IMG} --build-arg VERSION=${DOCKER_TAG} .

.PHONY: docker-publish
docker-publish:
	docker image push ${DOCKER_IMG}
