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
sudo docker build -t robocker/tankbasic -f DockerfileDev .
sudo docker run -p 49153:80 -it --rm --name tank-1 robocker/tankbasic:latest
sudo docker network connect robocker-net tank-1
```

Running inside server
```
DEBUG=tankBasic:* node index.js
```

Inspiration links: https://nodejs.org/en/docs/guides/nodejs-docker-webapp/