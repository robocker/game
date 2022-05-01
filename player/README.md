Running localy with debugger

```
DEBUG=player:* node index.js
```

Creating docker container:

```
# on Windows
yarn build
# on docker host- linux
sudo docker build -t robocker/player .
```


Running docker container:
```
sudo docker run -p 3000:3000 -d --rm --name player robocker/player:latest
```
Running with added directory (run it in player folder)
```
sudo docker run -p 3000:3000 -d --rm --name player-1 -v "$PWD":/usr/src/app robocker/player:latest
```
Connecting with robocker-net
```
sudo docker network connect robocker-net player-1
```

Running with cpu limitation
```
sudo docker run --cpu-quota=1000 robocker/player:latest
```

Running dev dockerfile
```
sudo docker build -t robocker/player_dev -f DockerfileDev  .
sudo docker run -p 3000:3000 -it --rm --name player-1 -v "$PWD":/usr/src/app robocker/player_dev:latest
DEBUG=player:* node index.js
#second terminal:
sudo docker network connect robocker-net player-1
```

Entering into container
```
sudo docker exec -it player-1 /bin/bash
```

Inspiration links: https://nodejs.org/en/docs/guides/nodejs-docker-webapp/