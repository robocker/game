const debug = require("debug")("engineMock:index.js");
const WebSocket = require("ws");

debug("start mock engine");

const server = new WebSocket.Server({
  port: 8080,
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

setInterval(() => {
  const msg = {
    tanks: [
      {
        id: 1,
        playerId: 1,
        angle: Math.PI / 4,
        turret: {
          angle: 0,
          angleVertical: Math.PI / 8,
        },
        x: -10,
        y: 10,
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
    ],
  };

//   debug(JSON.stringify(msg));
  sockets.forEach((s) => s.send(JSON.stringify(msg)));
}, 1000);
