import * as BABYLON from "babylonjs";
import "babylonjs-loaders";
import { SceneCreator } from "./SceneCreator";
import { GameManager } from "./GameManager";

const sceneCreator = new SceneCreator("renderCanvas", sceneCallback);
const gameManager = new GameManager(sceneCreator);

function sceneCallback(scene) {
  gameManager.addTank();

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
  switch (event.code) {
    case "KeyD":
      gameManager.addTank();
      break;
    default:
      const tankCount = gameManager.getTanksCount();
      gameManager.moveTank(tankCount - 1);
  }
});
