Making package:
```
mvnw package && java -jar target/engine-0.0.1-SNAPSHOT.jar
```
Creating docker container
```
sudo docker build -t robocker/engine .
```

Simple running (on port 8070)
```
sudo docker run -p 8070:8080 robocker/engine
```
