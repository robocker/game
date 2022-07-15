FROM openjdk:15-jdk-alpine

WORKDIR /usr/src

COPY ./src ./src
COPY ./.mvn ./.mvn
COPY ./mvnw ./mvnw
COPY ./pom.xml ./pom.xml

RUN ./mvnw package
RUN cd ./target
RUN 'ls'
# ARG JAR_FILE=target/*.jar

ENTRYPOINT ["java","-jar","/usr/src/target/engine-0.0.1-SNAPSHOT.jar"]