const debug = require("debug")("engineMock:index.js");
const WebSocket = require("ws");
const express = require("express");

const app = express();

var expressWs = require("express-ws")(app);

app.use(express.json());

const PORT = 8080;
let sockets = [];

let angle4 = 0;
let explosionTimer = 0;
let lifeLevel = 4;

let tanksP1 = [
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
];

let tanksP2 = [
  {
    id: "3",
    playerId: 2,
    angle: (-Math.PI * 3) / 4,
    imageName: "robocker/tankbasic",
    insidePortNumber: 80,
    turret: {
      angle: -Math.PI / 4,
      angleVertical: Math.PI / 6,
    },
    x: -10,
    z: 20,
    lifeLevel,
  },
];

let bullets = [
  {
    angle: angle4,
    gravitationSpeed: 7.071067811865475,
    tankId: 4,
    verticalAngle: 0.7853981633974483,
    x: 15,
    z: 5,
    y: 1.1,
  },
];

const state = {
  players: [
    {
      current: true,
      color: { r: 0, g: 0, b: 1 },
      id: 1,
      tanks: tanksP1,
    },
    {
      current: false,
      color: { r: 1, g: 0, b: 0 },
      id: 2,
      tanks: tanksP2,
    },
  ],
};

app.get("/api/info", (req, resp) => {
  console.log("/api/info");
  resp.status(200);
  resp.send(state);
});

app.ws("/state", function (ws, req) {
  sockets.push(ws);

  ws.on("message", function (msg, socket) {
    console.log(msg);
  });

  ws.on("close", function () {
    sockets = sockets.filter((s) => s !== ws);
  });
});

app.listen(PORT, (error) => {
  if (!error) {
    console.log("It's working!");
  } else {
    console.log(error);
  }
});

debug("start mock engine");

setInterval(() => {
  angle4 += Math.PI / 12;
  if (angle4 > Math.PI * 2) {
    angle4 = 0;
  }

  if (lifeLevel > 0) {
    lifeLevel--;
  }

  tanksP1[2].angle = angle4;
  tanksP2[0].lifeLevel = lifeLevel;

  const msg = {
    tanks: [...tanksP1, ...tanksP2],
    bullets: bullets,
    explosions: [{ x: 17, z: 10, tankId: 1, timer: explosionTimer, id: 0 }],
  };

  explosionTimer++;

  sockets.forEach((s) => s.send(JSON.stringify(msg)));
}, 1000);
