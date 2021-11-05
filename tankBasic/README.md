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
sudo docker run -p :80 -d --rm --name tank-1 robocker/tankbasic:latest
sudo docker network connect robocker-net tank-1
```

Inspiration links: https://nodejs.org/en/docs/guides/nodejs-docker-webapp/