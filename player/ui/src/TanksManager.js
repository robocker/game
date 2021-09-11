import * as BABYLON from "babylonjs";

export class TanksManager {
  meshesContainer;
  initPromise;
  sceneCreator;

  constructor(sceneCreator) {
    this.sceneCreator = sceneCreator;
  }

  initResources() {
    this.initPromise = new Promise((resolve, reject) => {
      BABYLON.SceneLoader.LoadAssetContainer(
        "assets/",
        "tank.babylon",
        this.sceneCreator.scene,
        (container) => {
          this.meshesContainer = container;
          resolve();
        }
      );
    });
  }

  createTank() {
    if (!this.initPromise) {
      this.initResources();
    }

    return new Promise((resolve, reject) => {
      this.initPromise.then(() => {
        const SPS = new BABYLON.SolidParticleSystem(
          "SPS",
          this.sceneCreator.scene
        );

        var top = BABYLON.Mesh.MergeMeshes([
          this.meshesContainer.meshes[1].clone(),
          this.meshesContainer.meshes[2].clone(),
        ]);

        var bottom = this.meshesContainer.meshes[0].clone();

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

        resolve(SPS);
      });
    });
  }
}
