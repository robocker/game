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

    // this.createCamera(scene);

    var camera = new BABYLON.FreeCamera(
        "camera1",
        new BABYLON.Vector3(0, 50, -10),
        scene
      );

      camera.setTarget(new BABYLON.Vector3(50, 0, 50));

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

  //TODO: Fix it- this camera settings caused:
  //- it's hard to select tank
  //- sometimes keys are blocked- it's hard to stop camera
  createCamera = function (scene) {
    var camera = new BABYLON.UniversalCamera(
      "MyCamera",
      new BABYLON.Vector3(0, 1, 0),
      scene
    );
    camera.minZ = 0.0001;
    camera.attachControl(this.canvas, true);
    camera.speed = 0.02;
    camera.angularSpeed = 0.05;
    camera.angle = Math.PI;
    camera.direction = new BABYLON.Vector3(
      Math.cos(camera.angle),
      0,
      Math.sin(camera.angle)
    );

    camera.inputs.removeByType("FreeCameraKeyboardMoveInput");
    camera.inputs.add(new FreeCameraKeyboardWalkInput());
  };
}

var FreeCameraKeyboardWalkInput = function () {
  this._keys = [];
  this.keysUp = [87, 38];
  this.keysDown = [83, 40];
  this.keysLeft = [65, 37];
  this.keysRight = [68, 39];
};

FreeCameraKeyboardWalkInput.prototype.attachControl = function (
  noPreventDefault
) {
  var _this = this;
  var engine = this.camera.getEngine();
  var element = engine.getInputElement();
  if (!this._onKeyDown) {
    element.tabIndex = 1;
    this._onKeyDown = function (evt) {
      if (
        _this.keysUp.indexOf(evt.keyCode) !== -1 ||
        _this.keysDown.indexOf(evt.keyCode) !== -1 ||
        _this.keysLeft.indexOf(evt.keyCode) !== -1 ||
        _this.keysRight.indexOf(evt.keyCode) !== -1
      ) {
        var index = _this._keys.indexOf(evt.keyCode);
        if (index === -1) {
          _this._keys.push(evt.keyCode);
        }
        if (!noPreventDefault) {
          evt.preventDefault();
        }
      }
    };
    this._onKeyUp = function (evt) {
      if (
        _this.keysUp.indexOf(evt.keyCode) !== -1 ||
        _this.keysDown.indexOf(evt.keyCode) !== -1 ||
        _this.keysLeft.indexOf(evt.keyCode) !== -1 ||
        _this.keysRight.indexOf(evt.keyCode) !== -1
      ) {
        var index = _this._keys.indexOf(evt.keyCode);
        if (index >= 0) {
          _this._keys.splice(index, 1);
        }
        if (!noPreventDefault) {
          evt.preventDefault();
        }
      }
    };
    element.addEventListener("keydown", this._onKeyDown, false);
    element.addEventListener("keyup", this._onKeyUp, false);
  }
};

FreeCameraKeyboardWalkInput.prototype.detachControl = function () {
  var engine = this.camera.getEngine();
  var element = engine.getInputElement();
  if (this._onKeyDown) {
    element.removeEventListener("keydown", this._onKeyDown);
    element.removeEventListener("keyup", this._onKeyUp);
    BABYLON.Tools.UnregisterTopRootEvents(canvas, [
      { name: "blur", handler: this._onLostFocus },
    ]);
    this._keys = [];
    this._onKeyDown = null;
    this._onKeyUp = null;
  }
};

FreeCameraKeyboardWalkInput.prototype.checkInputs = function () {
  if (this._onKeyDown) {
    var camera = this.camera;
    for (var index = 0; index < this._keys.length; index++) {
      var keyCode = this._keys[index];
      var speed = camera.speed;
      if (this.keysLeft.indexOf(keyCode) !== -1) {
        camera.direction.copyFromFloats(-speed, 0, 0);
      } else if (this.keysUp.indexOf(keyCode) !== -1) {
        camera.direction.copyFromFloats(0, 0, speed);
      } else if (this.keysRight.indexOf(keyCode) !== -1) {
        camera.direction.copyFromFloats(speed, 0, 0);
      } else if (this.keysDown.indexOf(keyCode) !== -1) {
        camera.direction.copyFromFloats(0, 0, -speed);
      }
      if (camera.getScene().useRightHandedSystem) {
        camera.direction.z *= -1;
      }
      camera.getViewMatrix().invertToRef(camera._cameraTransformMatrix);
      BABYLON.Vector3.TransformNormalToRef(
        camera.direction,
        camera._cameraTransformMatrix,
        camera._transformedDirection
      );
      camera.cameraDirection.addInPlace(camera._transformedDirection);
    }
  }
};

FreeCameraKeyboardWalkInput.prototype._onLostFocus = function (e) {
  this._keys = [];
};

FreeCameraKeyboardWalkInput.prototype.getClassName = function () {
  return "FreeCameraKeyboardWalkInput";
};

FreeCameraKeyboardWalkInput.prototype.getSimpleName = function () {
  return "keyboard";
};
