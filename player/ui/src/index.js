import * as BABYLON from "babylonjs";
import "babylonjs-loaders";
import { SceneCreator } from './SceneCreator';


var createScene = function (engine) {
    var scene = new BABYLON.Scene(engine);

    var camera = new BABYLON.FreeCamera(
      "camera1",
      new BABYLON.Vector3(0, 5, -10),
      scene
    );

    camera.setTarget(BABYLON.Vector3.Zero());

    camera.attachControl(sceneCreator.canvas, true);

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

    const ground = BABYLON.MeshBuilder.CreateGroundFromHeightMap("gdhm", "assets/heightMap.png", {width:512, height :512, subdivisions: 512, maxHeight: 5}, scene);

    ground.position.y = -2.1;
    return scene;
  };


const sceneCreator = new SceneCreator("renderCanvas", createScene);

var SPSs = [];
var meshesContainer;

var addTank = function(){
    const SPS = new BABYLON.SolidParticleSystem("SPS", sceneCreator.scene);

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
        turret.position.y = 0.0;
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



sceneCreator.init();

// Resize
window.addEventListener("resize", function () {
    sceneCreator.engine.resize();
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
