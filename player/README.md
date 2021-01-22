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
sudo docker run  -p 3000:3000 --cpu-period=400000 --cpu-quota=1000 robocker/player:latest
```

Inspiration links: https://nodejs.org/en/docs/guides/nodejs-docker-webapp/