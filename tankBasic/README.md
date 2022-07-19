# Working on Docker #
## Creating docker container ##
```
sudo docker build -t robocker/tankbasic .
```

## Running docker container##
In normal flow 'engine' is responsible for running other containers but you can run it manually for testing if you wish.

```
sudo docker run -p 49153:80 -d --rm --name tank-1 robocker/tankbasic:latest
sudo docker network connect robocker-net tank-1
```
## Entering into container ##
```
sudo docker exec -it tank-1 /bin/bash
```

## Creating dev container ##

To be able to see changes without creating new container. Only restart node server is currently required.

```
sudo docker build -t robocker/tankbasic-dev -f DockerfileDev .
sudo docker run -p 49153:80 -d --rm --name tank-1 -v "$PWD":/usr/src/app robocker/tankbasic-dev:latest  tail -F /dev/null
sudo docker network connect robocker-net tank-1
sudo docker exec -ti tank-1 bash
DEBUG=tankBasic:* node index.js
```
# Worning on local environment #

To run this project locally you need to install node (https://nodejs.org/en/download/) LTS version should be ok. Later you need to install dependencies. Run in terminal in tankBasic folder:
```
npm install
```

## Running localy with debugger ##

```
npm run start
```
To make it works locally you need working engine. For example engineMock or if you have runned engine (locally or in docker). You only need to change constant urlBase to 'localhost' in tankBasic/index.js.

`urlBase = "engine"` is prepared to works inside docker. But maybe better would be run this project by tests locally?

## Running tests ##
```
npm run test
```

Inspiration links: https://nodejs.org/en/docs/guides/nodejs-docker-webapp/