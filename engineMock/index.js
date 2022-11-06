const debug = require("debug")("engineMock:index.js");
const WebSocket = require("ws");

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

setInterval(() => {
  angle4 += Math.PI / 12;
  if (angle4 > Math.PI * 2) {
    angle4 = 0;
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
        y: 0,
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
        y: 0,
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
        y: 0,
      },
    ],
    bullets: [
      {
        angle: angle4,
        gravitationSpeed: 7.071067811865475,
        tankId: 4,
        verticalAngle: 0.7853981633974483,
        x: 15,
        y: 5,
        z: 1.1,
      },
    ],
  };

  //   debug(JSON.stringify(msg));
  sockets.forEach((s) => s.send(JSON.stringify(msg)));
}, 1000);
