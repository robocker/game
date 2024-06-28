# Creating image #
There is two options:
## Compilation during bulding image ##

Creating docker container
```
docker build -t robockergame/engine .
```

## Compilation by Intellij ##
Making package (In IntelliJ Terminal):
```
./mvnw.cmd package
# and run:
./mvnw.cmd package && java -jar target/engine-0.0.1-SNAPSHOT.jar
```

```
docker build -t robockergame/engine -f DockerfileDev .
```


# Simple running #
## Start engine ##

Unfortunately java library which we use not pull images from docker (or I don't know how to configure it for now :))so unless you not created images locally you should pull it from docker repo:
```
docker pull robockergame/tankpython
docker pull robockergame/tankbasic
docker pull robockergame/player

```
running game:
```
docker run --rm --name engine -p 8080:8080 -v /var/run/docker.sock:/var/run/docker.sock robockergame/engine
```
You can check if works in browser: http://localhost:8080/

## Run demo game ##

Just open: http://localhost:8080/containers/demo?tanks=robockergame/tankbasic,robockergame/tankplayground

OR

http://localhost:8080/containers/demo?tanks=remote,remote


Where robockergame/tankbasic,robockergame/tankpython is tank container which will be created for player 1 (tankbasic) and player2 (tankpython). You can adjust it to run your image.

Then you can open players` views using returned ports e.g. for player1 it should be http://localhost:3000/

# Stopping all containers #
```
docker stop $(docker ps -a -q)
```

## Articles ##

Running containers by Java:
  * https://www.baeldung.com/docker-java-api
  * https://stackoverflow.com/questions/40961073/starting-and-stopping-docker-container-from-other-container