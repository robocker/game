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
sudo docker run --rm --name engine -p 8070:8080 -v /var/run/docker.sock:/var/run/docker.sock robocker/engine
```



### Articles: ###

Running containers by Java:
  * https://www.baeldung.com/docker-java-api
  * https://stackoverflow.com/questions/40961073/starting-and-stopping-docker-container-from-other-container