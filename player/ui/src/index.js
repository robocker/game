import * as BABYLON from "babylonjs";
import 'babylonjs-loaders';

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

  var sphere = BABYLON.MeshBuilder.CreateSphere(
    "sphere",
    { diameter: 2, segments: 32 },
    scene
  );

  sphere.position.y = 1;

  var ground = BABYLON.MeshBuilder.CreateGround(
    "ground",
    { width: 6, height: 6 },
    scene
  );

//   BABYLON.SceneLoader.Append("https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/BoomBox/glTF/", "BoomBox.gltf", scene, function (scene) {
//     // Create a default arc rotate camera and light.
//     scene.createDefaultCameraOrLight(true, true, true);

//     // The default camera looks at the back of the asset.
//     // Rotate the camera by 180 degrees to the front of the asset.
//     // scene.activeCamera.alpha += Math.PI;
// });

BABYLON.SceneLoader.ImportMesh("", "https://playground.babylonjs.com/scenes/", "skull.babylon", scene, function (newMeshes) {
    // Set the target of the camera to the first imported mesh
    camera.target = newMeshes[0];
});

// createSceneLoader();
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

const createSceneLoader = () => {
    let loadedModel = { status: "loading" };
    const sceneFilename = "BoomBox.gltf";
    const rootUrl =
      "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/BoomBox/glTF/";

    let loader = BABYLON.SceneLoader.ImportMesh(
      ["mesh1"],
      rootUrl,
      sceneFilename,
      scene,
      (meshes, particleSystems, skeletons, animationGroups) => {
        loadedModel.rootMesh = new BABYLON.AbstractMesh(
          sceneFilename + "-root-model",
          scene
        );
        if (opts.alwaysSelectAsActiveMesh === true) {
          loadedModel.rootMesh.alwaysSelectAsActiveMesh = true;
        }

        loadedModel.meshes = [];
        meshes.forEach((mesh) => {
          loadedModel.meshes.push(mesh);

          // leave meshes already parented to maintain model hierarchy:
          if (!mesh.parent) {
            mesh.parent = loadedModel.rootMesh;
          }

          if (opts.receiveShadows === true) {
            mesh.receiveShadows = true;
          }
        });
        loadedModel.particleSystems = particleSystems;
        loadedModel.skeletons = skeletons;
        loadedModel.animationGroups = animationGroups;

        loadedModel.status = LoaderStatus.Loaded;

        if (opts.scaleToDimension) {
          loadedModel.scaleTo(opts.scaleToDimension);
        }
        if (options.onModelLoaded) {
          options.onModelLoaded(loadedModel);
        }

        const originalDispose = loadedModel.dispose;
        loadedModel.dispose = () => {
          // console.log('Clearing cache (cannot re-use meshes).');
          suspenseCache[suspenseKey] = undefined;
          originalDispose.call(loadedModel);
        };

        resolve(loadedModel);
      },
      (event) => {
        if (opts.reportProgress === true && sceneLoaderContext !== undefined) {
          sceneLoaderContext.updateProgress(event);
        }
        if (opts.onLoadProgress) {
          opts.onLoadProgress(event);
        }
      },
      (_, message, exception) => {
        if (opts.onModelError) {
          opts.onModelError(loadedModel);
        }
        reject(exception ? message : "");
      },
      pluginExtension
    );

    if (loader) {
      loadedModel.loaderName = loader.name;
    } else {
      loadedModel.loaderName = "no loader found";
    }
};
