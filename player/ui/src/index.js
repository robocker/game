import * as BABYLON from "babylonjs";
import 'babylonjs-loaders';
import { Vector3 } from "babylonjs";

var canvas = document.getElementById("renderCanvas");

var engine = null;
var scene = null;
var sceneToRender = null;
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

BABYLON.SceneLoader.LoadAssetContainer("assets/", "tank.babylon", scene, (container)=> {
    const SPS = new BABYLON.SolidParticleSystem("SPS", scene);


    // SPS.addShape(sphere, 1); // 20 spheres


    // scene.meshes.push(container.meshes[1]);
    SPS.addShape(container.meshes[0], 1);
    SPS.addShape(container.meshes[1], 1);
    SPS.addShape(container.meshes[2], 1);
    // SPS.addShape(container.meshes[2], 1);
    // SPS.addShape(container.meshes[3], 1);
    // container.meshes.forEach((mesh, index)=>
    // {
    //     console.log("mesh",mesh);
    //     // SPS.addShape(mesh, 1);

    //     scene.meshes.push(mesh);

    //     // var clon = mesh.clone('aaa'+ index);
    //     // clon.x += 5;

    // })


    const mesh = SPS.buildMesh()
    SPS.initParticles = () => {
        // debugger;
        // for (let p = 0; p < SPS.nbParticles; p++) {
            // const particle = SPS.particles[p];
            // particle.position.x = BABYLON.Scalar.RandomRange(-5, 5);
            // particle.position.y = BABYLON.Scalar.RandomRange(-2, 2);
            // particle.position.z = BABYLON.Scalar.RandomRange(-2, 2);
            // particle.color = new BABYLON.Color3(Math.random(), Math.random(), Math.random());
        // }
        // if(SPS.nbParticles>2){

        // }
        const bottom = SPS.particles[0];
        bottom.position.x = 0;
        bottom.position.y = 0;
        bottom.position.z = 0;
        bottom.color = new BABYLON.Color3(Math.random(), Math.random(), Math.random());

        const turret = SPS.particles[1];
        turret.position.y = 0.6;
        turret.position.x = -0.2;
        turret.position.z = 0.1;
        turret.color = new BABYLON.Color3(Math.random(), Math.random(), Math.random());

        const cannon = SPS.particles[2];
        cannon.rotation.z = -Math.PI/2;
        cannon.position.y = 0.62;
        cannon.position.x = 1;
        cannon.position.z = 0.1;
        cannon.color = new BABYLON.Color3(Math.random(), Math.random(), Math.random());

    };

    //Update SPS mesh
    SPS.initParticles();
    SPS.setParticles();

});


//////////////////
// const SPS = new BABYLON.SolidParticleSystem("SPS", scene);
// const sphere = BABYLON.MeshBuilder.CreateSphere("s", {});

// SPS.addShape(sphere, 1); // 20 spheres

// sphere.dispose(); //dispose of original model sphere

// const mesh = SPS.buildMesh(); // finally builds and displays the SPS mesh

// // initiate particles function
// SPS.initParticles = () => {
//     for (let p = 0; p < SPS.nbParticles; p++) {
//         const particle = SPS.particles[p];
//         particle.position.x = BABYLON.Scalar.RandomRange(-5, 5);
//         particle.position.y = BABYLON.Scalar.RandomRange(-2, 2);
//         particle.position.z = BABYLON.Scalar.RandomRange(-2, 2);
//         particle.color = new BABYLON.Color3(Math.random(), Math.random(), Math.random());
//     }
// };

// //Update SPS mesh
// SPS.initParticles();
// SPS.setParticles();

//////////////////////

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