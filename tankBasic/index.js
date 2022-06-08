const express = require("express");
const app = express();
const port = 80;
const debug = require("debug")("tankBasic:index.js");
const axios = require("axios");
const path = require("path");
const Tank = require("./Tank");
const WebSocket = require("ws");

app.use(express.static("frontend/build"));
app.use(express.json());

 const engineUrl = "http://engine:8080";
//const engineUrl = "http://localhost:8080/";

console.log(Tank);
const tank = new Tank();

axios.get(engineUrl + "/tank/id").then(
  (springMsg) => {
    debug(springMsg.data);
    tank.init(springMsg.data);
  },
  (error) => {
    debug(error);
  }
);

app.get("/", (req, res) => {
  debug("Tank base url");
  res.json({ msg: "I'm tank" });
});

app.post("/move", (req, res) => {
  debug("Tank move");
  debug(req.body);

  axios.patch(engineUrl + "/tank/move", { ...req.body }).then(
    (springMsg) => {
      debug(springMsg.data);

      res.json(springMsg.data);
    },
    (error) => {
      res.json({ msg: error });
    }
  );
});

app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`);
});

connectWebsocket();

async function connectWebsocket() {
  let clients = [
    new WebSocket("ws://localhost:8080"),
    new WebSocket("ws://localhost:8080"),
  ];

  clients.map((client) => {
    client.on("message", (msg) => console.log(msg.toString()));
  });

  // Wait for the client to connect using async/await
  await new Promise((resolve) => clients[0].once("open", resolve));

  // Prints "Hello!" twice, once for each client.
  clients[0].send("Hello!");
}
