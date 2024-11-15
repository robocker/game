const debug = require("debug")("commander:index.js");
const WebSocket = require("ws");

const server = new WebSocket.Server({
  port: 8070,
  path: "/connection",
});
let sockets = [];
server.on("connection", function (socket) {
  sockets.push(socket);

  socket.on("message", function (msg) {
    debug(msg.toString());
  });

  // When a socket closes, or disconnects, remove it from the array.
  socket.on("close", function () {
    sockets = sockets.filter((s) => s !== socket);
  });
});

setInterval(() => {
  const msg = [
    {
      tankId: 1,
      actions: [
        {
          angle: Math.PI,
          distance: 10,
          turret: {
            angle: -Math.PI,
            verticalAngle: Math.PI / 4,
            shoot: 'END_OF_ACTION',
          },
        },
      ],
    },
    {
      tankId: 2,
      actions: [
        {
          angle: Math.PI/3,
          distance: 10,
          turret: {
            angle: -Math.PI,
            verticalAngle: Math.PI / 4,
            shoot: 'END_OF_ACTION',
          },
        },
      ],
    },
    {
      tankId: 4,
      actions: [
        {
          angle: Math.PI/2,
          distance: 10,
          turret: {
            angle: -Math.PI,
            verticalAngle: Math.PI / 4,
            shoot: 'END_OF_ACTION',
          },
        },
      ],
    },
  ];

  //   debug(JSON.stringify(msg));
  sockets.forEach((s) => s.send(JSON.stringify(msg)));
}, 2500);
