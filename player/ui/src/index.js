import * as BABYLON from 'babylonjs';
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

  return scene;
};
window.initFunction = async function () {
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
  const taskPromise = new Promise((resolve, reject) => {
    let loadedModel = new LoadedModel();

    loadedModel.status = LoaderStatus.Loading;

    let loader = SceneLoader.ImportMesh(
      opts.meshNames,
      rootUrl,
      sceneFilename,
      scene,
      (meshes, particleSystems, skeletons, animationGroups) => {
        loadedModel.rootMesh = new AbstractMesh(
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
  });

  let result;
  let error = null;
  let suspender = (async () => {
    try {
      result = await taskPromise;
    } catch (e) {
      error = e;
    } finally {
      suspender = null;
    }
  })();

  const getAssets = () => {
    if (suspender) {
      throw suspender;
    }
    if (error !== null) {
      throw error;
    }

    return result;
  };
  return getAssets;
};
