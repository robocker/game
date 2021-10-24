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

  createTank(tankData) {
    if (!this.initPromise) {
      this.initResources();
    }

    return new Promise((resolve, reject) => {
      this.initPromise.then(() => {
        const sps = new BABYLON.SolidParticleSystem(
          tankData.containerName,
          this.sceneCreator.scene,
          {isPickable: true}
        );

        var top = BABYLON.Mesh.MergeMeshes([
          this.meshesContainer.meshes[1].clone(),
          this.meshesContainer.meshes[2].clone(),
        ]);

        var bottom = this.meshesContainer.meshes[0].clone();

        sps.addShape(bottom, 1);
        sps.addShape(top, 1);

        bottom.dispose();
        top.dispose();

        const mesh = sps.buildMesh();
        sps.initParticles = () => {
          const bottom = sps.particles[0];
          bottom.position.x = 0;
          bottom.position.y = 0;
          bottom.position.z = 0;
          bottom.color = new BABYLON.Color3(
            Math.random(),
            Math.random(),
            Math.random()
          );

          const turret = sps.particles[1];
          turret.position.y = 0.0;
          turret.position.x = -0.2;
          turret.position.z = 0.1;
          turret.color = new BABYLON.Color3(
            Math.random(),
            Math.random(),
            Math.random()
          );
        };

        sps.vars.tankData =tankData;

        sps.initParticles();

        sps.setParticles();

        resolve(sps);
      });
    });
  }
}
