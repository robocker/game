import { LogManager } from "./LogManager";

export class SceneCreator {
  canvas;
  engine;
  scene;
  sceneCallback;

  constructor(canvasSelector, sceneCallback) {
    this.canvas = document.getElementById(canvasSelector);
    this.sceneCallback = sceneCallback;
  }

  createScene(engine) {
    var scene = new BABYLON.Scene(engine);

    var camera = new BABYLON.FreeCamera(
      "camera1",
      new BABYLON.Vector3(0, 20, -1),
      scene
    );

    camera.setTarget(BABYLON.Vector3.Zero());

    camera.attachControl(this.canvas, true);

    var light = new BABYLON.HemisphericLight(
      "light",
      new BABYLON.Vector3(0, 1, 0),
      scene
    );

    light.intensity = 0.7;

    return scene;
  }

  initFunction = async function () {
    var asyncEngineCreation = async function () {
      try {
        return this.createDefaultEngine();
      } catch (e) {
        LogManager.instance.console.error(
          "the available createEngine function failed. Creating the default engine instead"
        );
        return this.createDefaultEngine();
      }
    }.bind(this);

    this.engine = await asyncEngineCreation();
    if (!this.engine) throw "engine should not be null.";
    this.scene = this.createScene(this.engine);
    this.sceneCallback(this.scene);
  };

  init() {
    this.initFunction().then(() => {
      let sceneToRender = this.scene;
      this.engine.runRenderLoop(function () {
        if (sceneToRender && sceneToRender.activeCamera) {
          sceneToRender.render();
        }
      });
    });
  }

  createDefaultEngine = function () {
    return new BABYLON.Engine(this.canvas, true, {
      preserveDrawingBuffer: true,
      stencil: true,
      disableWebGL2Support: false,
    });
  };
}
