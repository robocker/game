
# Creating docker container #

```
docker build -t robocker/player .
```

# Running #

In normal flow 'engine' is responsible for running other containers but you can run it manually for testing if you wish.

Running docker container:
```
docker run -p 3000:3000 -d --rm --name player robocker/player:latest
```
Running with added directory (run it in player folder)
```
docker run -p 3000:3000 -d --rm --name player-1 -v "$PWD":/usr/src/app robocker/player:latest
```
Connecting with robocker-net
```
docker network connect robocker-net player-1
```

Running with cpu limitation
```
docker run --cpu-quota=1000 robocker/player:latest
```

Running dev dockerfile
```
docker build -t robocker/player_dev -f DockerfileDev  .
docker run -p 3000:3000 -it --rm --name player-1 -v "$PWD":/usr/src/app robocker/player_dev:latest
DEBUG=player:* node index.js
#second terminal:
docker network connect robocker-net player-1
```

Entering into container
```
docker exec -it player-1 /bin/bash
```

# Running localy with debugger (e.g. in Git bash) #

Download dependencies and build ui
```
yarn
yarn build
```
Running
```
DEBUG=player:* node index.js
```
Inspiration links: https://nodejs.org/en/docs/guides/nodejs-docker-webapp/