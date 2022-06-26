Running localy with debugger

```
npm start
```

Creating docker container:

```
sudo docker build -t robocker/tankbasic .
```


Running docker container:
```
sudo docker run -p 49153:80 -d --rm --name tank-1 robocker/tankbasic:latest
sudo docker network connect robocker-net tank-1
```

Entering into container
```
sudo docker exec -it tank-1 /bin/bash
```

Creating dev container:

```
sudo docker build -t robocker/tankbasic-dev -f DockerfileDev .
sudo docker run -p 49153:80 -d --rm --name tank-1 -v "$PWD":/usr/src/app robocker/tankbasic-dev:latest  tail -F /dev/null
sudo docker network connect robocker-net tank-1
sudo docker exec -ti tank-1 bash
DEBUG=tankBasic:* node index.js
```
 robocker/player_dev:latest
Inspiration links: https://nodejs.org/en/docs/guides/nodejs-docker-webapp/