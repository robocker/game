Running localy with debugger

```
DEBUG=player:* node index.js
```

Creating docker container:

```
sudo docker build -t robocker/player .
```


Running docker container:
```
sudo docker run -p -rm 3000:3000 robocker/player:latest
```

Inspiration links: https://nodejs.org/en/docs/guides/nodejs-docker-webapp/