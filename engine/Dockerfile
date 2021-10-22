FROM openjdk:15-jdk-alpine
#RUN 'mvnw package && java -jar target/engine-0.0.1-SNAPSHOT.jar' #TODO: Build package while container building
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]