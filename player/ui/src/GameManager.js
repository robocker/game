import { TanksManager } from "./TanksManager";
import { AxiosManager } from "./AxiosRealManager";
import { Websocket } from "./Websocket";
import { Utils } from "./Utils";

export class GameManager {
  SPSs = {};
  players = [];

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
        this.players = this.players;

        for (let player of response.data.players) {

            if(player.current){
                const wrapper = document.getElementById("wrapper");
                const color = `rgb(${Utils.getColor(player.color.r)},${Utils.getColor(player.color.g)},${Utils.getColor(player.color.b)})`;
                wrapper.style.backgroundColor = color;
            }

          for (let tank of player.tanks) {
            this.addTank(tank, player);
          }

          Websocket.run(this);
        }
      })
      .catch(function (error) {
        console.log(error);
      });
  };

  addTank(tankData, player) {
    this.tanksManager.createTank(tankData, player).then((SPS) => {
      if (tankData) {
        SPS.mesh.position.z = tankData.y;
        SPS.mesh.position.x = tankData.x;
        SPS.mesh.rotation.y = tankData.angle;
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

      this.SPSs[tankData.id] = SPS;
    });
  }

  getTanksCount() {
    return this.SPSs.length;
  }

  updateTanks(data) {
    for (let tankData of data.tanks) {
      const tank = this.SPSs[tankData.id];

      if (
        tank.mesh.position.z !== tankData.y ||
        tank.mesh.position.x !== tankData.x
      ) {
        tank.mesh.position.z = tankData.y;
        tank.mesh.position.x = tankData.x;

        tank.particles[1].rotation.y -= Math.PI / 180;
        tank.setParticles();
      }
    }
  }

  moveTank(index, tryb) {
    const tank = this.SPSs[index];

    tank.particles[1].rotation.y -= Math.PI / 180;
    tank.particles[2].rotation.y -= Math.PI / 180;
    tank.particles[2].rotation.z -= Math.PI / 180;
    // tank.mesh.rotation.y += Math.PI / 180;

    // if (tryb == "prawo") {
    //   tank.mesh.position.z += 2;
    // }
    // if (tryb == "lewo") {
    //   tank.mesh.position.z -= 2;
    // } else {
    //   tank.mesh.position.x += 2;
    // }

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
