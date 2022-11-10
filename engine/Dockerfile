FROM openjdk:15-jdk-alpine

WORKDIR /usr/src

COPY ./.mvn ./.mvn
COPY ./mvnw ./mvnw
COPY ./pom.xml ./pom.xml
COPY ./src/main/java/pl/pastmo/robocker/engine/EngineApplication.java ./src/main/java/pl/pastmo/robocker/engine/EngineApplication.java

RUN ./mvnw install

COPY ./src ./src

RUN ./mvnw package
RUN cd ./target
RUN 'ls'

ENTRYPOINT ["java","-jar","/usr/src/target/engine-0.0.1-SNAPSHOT.jar"]