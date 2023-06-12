# Creating image #
There is two options:
## Compilation during bulding image ##

Creating docker container
```
docker build -t robocker/engine .
```

## Compilation by Intellij ##
Making package (In IntelliJ Terminal):
```
./mvnw.cmd package
# and run:
./mvnw.cmd package && java -jar target/engine-0.0.1-SNAPSHOT.jar
```

```
docker build -t robocker/engine -f DockerfileDev .
```


# Simple running #
## Start engine ##

```
docker run --rm --name engine -p 8080:8080 -v /var/run/docker.sock:/var/run/docker.sock robocker/engine
```
You can check if works in browser: http://localhost:8080/

## Run demo game ##

Just open: http://localhost:8080/containers/demo?tanks=robocker/tankbasic,robocker/tankpython

Where robocker/tankbasic,robocker/tankpython is tank container which will be created for player 1 (tankbasic) and player2 (tankpython). You can adjust it to run your image.

Then you can open players` views using returned ports e.g. for player1 it should be http://localhost:3000/

# Stopping all containers #
```
docker stop $(docker ps -a -q)
```

## Articles ##

Running containers by Java:
  * https://www.baeldung.com/docker-java-api
  * https://stackoverflow.com/questions/40961073/starting-and-stopping-docker-container-from-other-container