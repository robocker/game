# Creating image #
There is two options:
## Compilation during bulding image ##

Creating docker container
```
sudo docker build -t robocker/engine .
```

## Compilation by Intellij ##
Making package (In IntelliJ Terminal):
```
./mvnw.cmd package
# and run:
./mvnw.cmd package && java -jar target/engine-0.0.1-SNAPSHOT.jar
```

```
sudo docker build -t robocker/engine -f DockerfileDev .
```


# Simple running #
## Start engine ##

First time is required to create network:
```
sudo docker network create -d bridge robocker-net
```
Later:
```
sudo docker run --rm --name engine -p 8080:8080 -v /var/run/docker.sock:/var/run/docker.sock robocker/engine
## in other terminal window:
sudo docker network connect --alias engine robocker-net engine
```
You can check if works in browser: http://localhost:8080/

## Run demo game ##

Just open: http://localhost:8080/containers/demo

Then you can open players` views using returned ports e.g. for player1 it should be http://localhost:3000/

# Stopping all containers #
```
sudo docker stop $(sudo docker ps -a -q)
```

## Articles ##

Running containers by Java:
  * https://www.baeldung.com/docker-java-api
  * https://stackoverflow.com/questions/40961073/starting-and-stopping-docker-container-from-other-container