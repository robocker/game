import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

import * as BABYLON from "babylonjs";
import "babylonjs-loaders";
import { SceneCreator } from "./SceneCreator";
import { GameManager } from "./GameManager";
import { LogManager } from "./LogManager";

const sceneCreator = new SceneCreator("renderCanvas", sceneCallback);
const gameManager = new GameManager(sceneCreator);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

function sceneCallback(scene) {

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
    LogManager.instance.debug(event.code);

    const tankCount = gameManager.getTanksCount();

  switch (event.code) {
    case "KeyD":
    //   gameManager.addTank();
      break;
    case "Digit1":

        // gameManager.moveTank(2, "prawo");
    break;
    case "Digit2":

        // gameManager1.moveTank(2, "lewo");
    break;
    default:

    //   gameManager.moveTank(tankCount - 1);
  }
});

