import * as BABYLON from "babylonjs";
import "babylonjs-loaders";
import { SceneCreator } from "./SceneCreator";
import { GameManager } from "./GameManager";
import { Websocket } from "./Websocket";

const sceneCreator = new SceneCreator("renderCanvas", sceneCallback);
const gameManager = new GameManager(sceneCreator);

function sceneCallback(scene) {

    Websocket.run();
    // var ws = new WebSocket("ws://localhost:3000/");

    // ws.onopen = function() {

    //    // Web Socket is connected, send data using send()
    //    let count = 0;
    //    setInterval(()=>{
    //        count++;
    //        ws.send(`Message to send${count}`);
    //    },700)
    // };

    // ws.onmessage = function (evt) {
    //    var received_msg = evt.data;
    //    console.log(received_msg);
    // };

    // ws.onclose = function() {

    //    // websocket is closed.
    //    console.log("Connection is closed...");
    // };


  gameManager.initGame();

  const ground = BABYLON.MeshBuilder.CreateGroundFromHeightMap(
    "gdhm",
    "assets/heightMap.png",
    { width: 512, height: 512, subdivisions: 512, maxHeight: 5 },
    scene
  );

  ground.position.y = -2.1;
}

sceneCreator.init();

// Resize
window.addEventListener("resize", function () {
  sceneCreator.engine.resize();
});

window.addEventListener("keypress", function (event) {
    console.log(event.code);

    const tankCount = gameManager.getTanksCount();

  switch (event.code) {
    case "KeyD":
    //   gameManager.addTank();
      break;
    case "Digit1":

        // gameManager.moveTank(tankCount - 1, "prawo");
    break;
    case "Digit2":

        // gameManager.moveTank(tankCount - 1, "lewo");
    break;
    default:

    //   gameManager.moveTank(tankCount - 1);
  }
});
