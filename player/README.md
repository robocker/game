# Creating docker container #

```
docker build -t robockergame/player .
```

# Running #

In normal flow 'engine' is responsible for running other containers but you can run it manually for testing if you wish.

Running docker container:
```
docker run -p 3000:3000 -d --rm --name player robockergame/player:latest
```
Running with added directory (run it in player folder)
```
docker run -p 3000:3000 -d --rm --name player-1 -v "$PWD":/usr/src/app robockergame/player:latest
```
Connecting with robocker-net
```
docker network connect robocker-net player-1
```

Running with cpu limitation
```
docker run --cpu-quota=1000 robockergame/player:latest
```

Running dev dockerfile
```
docker build -t robockergame/player_dev -f DockerfileDev  .
docker run -p 3000:3000 -it --rm --name player-1 -v "$PWD":/usr/src/app robockergame/player_dev:latest
DEBUG=player:* node index.js
#second terminal:
docker network connect robocker-net player-1
```

Entering into container
```
docker exec -it player-1 /bin/bash
```

# Running localy with mock (e.g. in Git bash) #

```
npm install
npm run start:mock
```

Open [http://localhost:3000](http://localhost:3000) to view it in your browser.


# `npm test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.
