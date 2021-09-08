export class SceneCreator {

    canvas;
    engine;
    scene;
    createSceneFunction;

    constructor(canvasSelector, sceneCallback){
        this.canvas = document.getElementById(canvasSelector);
        this.createSceneFunction = sceneCallback;
    }

    initFunction = async function () {
        var asyncEngineCreation = async function () {
          try {
            return this.createDefaultEngine();
          } catch (e) {
            console.log(
              "the available createEngine function failed. Creating the default engine instead"
            );
            return this.createDefaultEngine();
          }
        }.bind(this);

        this.engine = await asyncEngineCreation();
        if (!this.engine) throw "engine should not be null.";
        this.scene = this.createSceneFunction(this.engine);
      }

    init(){
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