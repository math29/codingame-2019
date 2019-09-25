FROM maven:3.6-jdk-8-alpine

COPY pom.xml /working_dir/

WORKDIR /working_dir

COPY src src/

RUN mvn -T1C -Pcoverage-per-test clean install -U