##################################################
# BUILDER CONTAINER
##################################################

FROM openjdk:11.0.12 as builder

USER root

WORKDIR /usr/src/dependencies

# INSTALL MAKE
RUN apt update \
    && apt install build-essential -y

# INSTALL SQLITE3
RUN wget https://www.sqlite.org/2021/sqlite-autoconf-3340100.tar.gz \
    && tar -zxf sqlite-autoconf-3340100.tar.gz \
    && cd sqlite-autoconf-3340100 \
    && ./configure \
    && make \
    && make install

# USER 'make' and 'sqlite3' to create the dev database
COPY Makefile Makefile

# TODO Uncomment these when default sqlite database has been added
# COPY database/sqlite database/sqlite
# RUN make sqlite-db-refresh

##################################################
# GRADLE CONTAINER
##################################################

FROM gradle:7.3.3-jdk11 as gradleimage

WORKDIR /home/gradle/source

COPY build.gradle build.gradle
COPY gradlew gradlew
COPY settings.gradle settings.gradle
COPY src src

RUN gradle wrapper

RUN ./gradlew bootJar

##################################################
# FINAL CONTAINER
##################################################

FROM adoptopenjdk/openjdk12:jre-12.0.2_10-alpine

USER root

ARG VERSION

WORKDIR /usr/src/app

# copy jar, dev db, and dev resource files
# TODO Uncomment these when default sqlite database has been added
# COPY --from=builder /usr/src/dependencies/ga4gh-starter-kit.dev.db ga4gh-starter-kit.dev.db

COPY --from=gradleimage /home/gradle/source/build/libs/ga4gh-starter-kit-data-connect-${VERSION}.jar ga4gh-starter-kit-data-connect.jar
COPY src/test/resources/ src/test/resources/

ENTRYPOINT ["java", "-jar", "ga4gh-starter-kit-data-connect.jar"]
