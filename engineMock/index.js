const debug = require("debug")("engineMock:index.js");
const WebSocket = require("ws");
const express = require("express");

const app = express();

app.use(express.json());

const PORT = 3000;

app.listen(PORT, (error) => {
  if (!error) {
    console.log("It's working!");
  } else {
    console.log(error);
  }
});

app.get("/tank/info", (req, resp) => {
  resp.status(200);
  resp.send({ playerId: 1, id: 4 });
});

debug("start mock engine");

const server = new WebSocket.Server({
  port: 8080,
  path: "/state",
});
let sockets = [];
server.on("connection", function (socket) {
  sockets.push(socket);

  socket.on("message", function (msg) {
    debug(msg);
  });

  // When a socket closes, or disconnects, remove it from the array.
  socket.on("close", function () {
    sockets = sockets.filter((s) => s !== socket);
  });
});

let angle4 = 0;
let explosionTimer = 0;
let lifeLevel = 4;

setInterval(() => {
  angle4 += Math.PI / 12;
  if (angle4 > Math.PI * 2) {
    angle4 = 0;
  }

  if (lifeLevel > 0) {
    lifeLevel--;
  }

  const msg = {
    tanks: [
      {
        id: 1,
        playerId: 1,
        angle: 0,
        turret: {
          angle: 0,
          angleVertical: 0,
        },
        x: -10,
        z: 0,
        lifeLevel,
      },
      {
        id: 2,
        playerId: 1,
        angle: Math.PI / 4,
        turret: {
          angle: Math.PI / 4,
          angleVertical: Math.PI / 4,
        },
        x: 0,
        z: 0,
        lifeLevel: 2,
      },
      {
        id: "3",
        angle: (-Math.PI * 3) / 4,
        turret: {
          angle: -Math.PI / 4,
          angleVertical: Math.PI / 6,
        },
        x: -10,
        z: 20,
        lifeLevel,
      },
      {
        id: 4,
        playerId: 1,
        angle: angle4,
        turret: {
          angle: Math.PI / 4,
          angleVertical: Math.PI / 4,
        },
        x: 10,
        z: 0,
        lifeLevel: 3,
      },
    ],
    bullets: [
      {
        angle: angle4,
        gravitationSpeed: 7.071067811865475,
        tankId: 4,
        verticalAngle: 0.7853981633974483,
        x: 15,
        z: 5,
        y: 1.1,
      },
    ],
    explosions: [{ x: 17, z: 10, tankId: 1, timer: explosionTimer, id: 0 }],
  };

  explosionTimer++;

  //   debug(JSON.stringify(msg));
  sockets.forEach((s) => s.send(JSON.stringify(msg)));
}, 1000);
