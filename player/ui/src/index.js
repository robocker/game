import * as BABYLON from "babylonjs";
import "babylonjs-loaders";
import { Vector3 } from "babylonjs";

var canvas = document.getElementById("renderCanvas");

var engine = null;
var scene = null;
var sceneToRender = null;
var SPSs = [];
var meshesContainer;

var addTank = function(){
    const SPS = new BABYLON.SolidParticleSystem("SPS", scene);

      var top = BABYLON.Mesh.MergeMeshes([
        meshesContainer.meshes[1].clone(),
        meshesContainer.meshes[2].clone(),
      ]);

      var bottom = meshesContainer.meshes[0].clone();

      SPS.addShape(bottom, 1);
      SPS.addShape(top, 1);

      bottom.dispose();
      top.dispose();

      const mesh = SPS.buildMesh();
      SPS.initParticles = () => {
        const bottom = SPS.particles[0];
        bottom.position.x = 0;
        bottom.position.y = 0;
        bottom.position.z = 0;
        bottom.color = new BABYLON.Color3(
          Math.random(),
          Math.random(),
          Math.random()
        );

        const turret = SPS.particles[1];
        turret.position.y = 0.1;
        turret.position.x = -0.2;
        turret.position.z = 0.1;
        turret.color = new BABYLON.Color3(
          Math.random(),
          Math.random(),
          Math.random()
        );
      };

      SPS.initParticles();
      SPS.setParticles();

      SPSs.push(SPS);
}

var createDefaultEngine = function () {
  return new BABYLON.Engine(canvas, true, {
    preserveDrawingBuffer: true,
    stencil: true,
    disableWebGL2Support: false,
  });
};
var createScene = function () {
  var scene = new BABYLON.Scene(engine);

  var camera = new BABYLON.FreeCamera(
    "camera1",
    new BABYLON.Vector3(0, 5, -10),
    scene
  );

  camera.setTarget(BABYLON.Vector3.Zero());

  camera.attachControl(canvas, true);

  var light = new BABYLON.HemisphericLight(
    "light",
    new BABYLON.Vector3(0, 1, 0),
    scene
  );

  light.intensity = 0.7;

  BABYLON.SceneLoader.LoadAssetContainer(
    "assets/",
    "tank.babylon",
    scene,
    (container) => {
        meshesContainer = container;

        addTank();

    }
  );

  return scene;
};
var initFunction = async function () {
  var asyncEngineCreation = async function () {
    try {
      return createDefaultEngine();
    } catch (e) {
      console.log(
        "the available createEngine function failed. Creating the default engine instead"
      );
      return createDefaultEngine();
    }
  };

  engine = await asyncEngineCreation();
  if (!engine) throw "engine should not be null.";
  scene = createScene();
};
initFunction().then(() => {
  sceneToRender = scene;
  engine.runRenderLoop(function () {
    if (sceneToRender && sceneToRender.activeCamera) {
      sceneToRender.render();
    }
  });
});

// Resize
window.addEventListener("resize", function () {
  engine.resize();
});

window.addEventListener("keypress", function (event) {
  switch (event.code) {
    case "KeyD":
        addTank();
      break;
    default:
      SPSs[SPSs.length-1].particles[1].rotation.y -= this.Math.PI / 180;
      SPSs[SPSs.length-1].mesh.rotation.y += this.Math.PI / 180;
      SPSs[SPSs.length-1].mesh.position.x += 0.1;

      SPSs[SPSs.length-1].setParticles();
  }
});
