import { TanksManager } from "./TanksManager";
import { AxiosManager } from "./AxiosRealManager";

export class GameManager {
  SPSs = [];

  sceneCreator;
  tanksManager;

  constructor(sceneCreator) {
    this.sceneCreator = sceneCreator;
    this.tanksManager = new TanksManager(this.sceneCreator);
  }

  initGame = () => {
    AxiosManager.get("/api/info", {})
      .then((response) => {
        console.log(response);

        for (let tank of response.data.tanks) {
          this.addTank(tank);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
  };

  addTank(tankData) {
    this.tanksManager.createTank(tankData).then((SPS) => {
      if (tankData) {
        SPS.mesh.position.z = tankData.y;
        SPS.mesh.position.x = tankData.x;
        SPS.setParticles();
      }

      this.sceneCreator.scene.onPointerObservable.add((pointerInfo) => {
        switch (pointerInfo.type) {
          case BABYLON.PointerEventTypes.POINTERDOWN:
            var pickResult = pointerInfo.pickInfo;
            var faceId = pickResult.faceId;
            if (faceId == -1) {
              return;
            }

            if (pointerInfo.pickInfo.pickedMesh === SPS.mesh) {
              SPS.vars.selected = true;
              console.log(tankData);
              console.log(SPS.vars);
            } else if (
              pointerInfo.pickInfo.pickedMesh.name == "gdhm" &&
              SPS.vars.selected
            ) {
              SPS.vars.selected = false;

              AxiosManager.post("/api/tanks/move", {
                ids: [SPS.vars.tankData.containerName.replace("tank-", "")],
                destination: {
                  x: pointerInfo.pickInfo.pickedPoint.x,
                  y: pointerInfo.pickInfo.pickedPoint.z,
                },
              })
                .then(function (response) {
                  console.log(response);
                })
                .catch(function (error) {
                  console.log(error);
                });
            }

            break;
        }
      });

      this.SPSs.push(SPS);
    });
  }

  getTanksCount() {
    return this.SPSs.length;
  }

  moveTank(index, tryb) {
    const tank = this.SPSs[index];

    tank.particles[1].rotation.y -= Math.PI / 180;
    tank.mesh.rotation.y += Math.PI / 180;

    if (tryb == "prawo") {
      tank.mesh.position.z += 2;
    }
    if (tryb == "lewo") {
      tank.mesh.position.z -= 2;
    } else {
      tank.mesh.position.x += 2;
    }

    tank.setParticles();

    AxiosManager.post("/api/tanks/move", {
      ids: [42, 11],
      destination: { x: 33, y: 98 },
    })
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.log(error);
      });
  }
}
