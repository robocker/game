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
sudo docker run -p :80 -d --rm --name tank-42 robocker/tankbasic:latest
sudo docker run -p :80 -d --rm --name tank-11 robocker/tankbasic:latest
```

Inspiration links: https://nodejs.org/en/docs/guides/nodejs-docker-webapp/