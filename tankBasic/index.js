const express = require("express");
const app = express();
const port = 80;
const debug = require("debug")("tankBasic:index.js");
const axios = require("axios");
const path = require("path");
const Tank = require("./src/Tank");
const WebSocket = require("ws");

app.use(express.static("frontend/build"));
app.use(express.json());

const urlBase = 'engine';
// const urlBase = 'localhost';

const engineUrl = `http://${urlBase}:8080/`;
const websocketUrl = `ws://${urlBase}:8080`;

const client = connectWebsocket();
const tank = new Tank(client);

axios.get(engineUrl + "/tank/info").then(
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

  tank.serveChangeDestination(req.body);
});

app.listen(port, () => {
  console.log(`Example app listening at port: ${port}`);
});

async function connectWebsocket() {
  let client = new WebSocket(websocketUrl);

  client.on("message", (msg) => tank.serveStateChange(msg.toString()));

  await new Promise((resolve) => client.once("open", resolve));

  return client;
}
