Making package (In IntelliJ Terminal):
```
mvnw package
# and run:
mvnw package && java -jar target/engine-0.0.1-SNAPSHOT.jar
```

Creating docker container
```
sudo docker build -t robocker/engine .
```

Simple running
```
sudo docker run --rm --name engine -p 8080:8080 -v /var/run/docker.sock:/var/run/docker.sock robocker/engine
#/http://localhost:8080/containers/demo create robocker-net network. You can run it first and later connect engine
sudo docker network connect --alias engine robocker-net engine
```

Stopping all containers:
```
sudo docker stop $(sudo docker ps -a -q)
```

### Articles: ###

Running containers by Java:
  * https://www.baeldung.com/docker-java-api
  * https://stackoverflow.com/questions/40961073/starting-and-stopping-docker-container-from-other-container