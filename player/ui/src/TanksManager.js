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

  createTank(tankData, player) {
    if (!this.initPromise) {
      this.initResources();
    }

    return new Promise((resolve, reject) => {
      this.initPromise.then(() => {
        const sps = new BABYLON.SolidParticleSystem(
          tankData.containerName,
          this.sceneCreator.scene,
          { isPickable: true }
        );

        var top = BABYLON.Mesh.MergeMeshes([
          this.meshesContainer.meshes[1].clone(),
        ]);

        var barrel = BABYLON.Mesh.MergeMeshes([
          this.meshesContainer.meshes[2].clone(),
        ]);

        var bottom = BABYLON.Mesh.MergeMeshes([
          this.meshesContainer.meshes[0].clone(),
        ]);

        sps.addShape(bottom, 1);
        sps.addShape(top, 1);
        sps.addShape(barrel, 1);

        bottom.dispose();
        top.dispose();
        barrel.dispose();

        sps.vars.destroyed = false;

        const mesh = sps.buildMesh();
        sps.initParticles = () => {
          const bottom = sps.particles[0];
          bottom.color = player.color;

          const turret = sps.particles[1];
          turret.color = player.color;

          const barrel = sps.particles[2];
          barrel.color = player.color;

          this.updateTankPosition(sps, tankData);
        };

        sps.updateParticle = function (p) {
          if (sps.vars.destroyed && sps.vars.destroyedStep <= 5) {
            p.rotation.x = (sps.vars.destroyedStep * Math.PI) / 2 / 5;

            if (sps.vars.destroyed && sps.vars.destroyedStep == 5) {
              const bottom = sps.particles[0];
              const turret = sps.particles[1];
              const barrel = sps.particles[2];

              const newColor = {
                r: bottom.color.r * 0.5,
                g: bottom.color.g * 0.5,
                b: bottom.color.b * 0.5,
              };

              bottom.color = newColor;
              turret.color = newColor;
              barrel.color = newColor;

              p.alive = false;
            }
            sps.vars.destroyedStep++;
          }

        };

        sps.vars.tankData = tankData;
        sps.vars.playerId = player.id;

        sps.initParticles();

        sps.setParticles();

        resolve(sps);
      });
    });
  }

  updateTankPosition(sps, tankData) {
    sps.mesh.position.z = tankData.z;
    sps.mesh.position.x = tankData.x;
    sps.mesh.rotation.y = -tankData.angle;

    const turret = sps.particles[1];
    turret.rotation.y = -tankData.turret.angle;

    const barrel = sps.particles[2];
    barrel.rotation.z = tankData.turret.angleVertical;
    barrel.rotation.y = -tankData.turret.angle;

  }
}
