import { TanksManager } from "./TanksManager";
import { AxiosManager } from "./AxiosRealManager";
import { Websocket } from "./Websocket";
import { Utils } from "./Utils";
import { LogManager } from "./LogManager";

export class GameManager {
  SPSs = {};
  players = [];
  bullets = [];

  sceneCreator;
  tanksManager;

  constructor(sceneCreator) {
    this.sceneCreator = sceneCreator;
    this.tanksManager = new TanksManager(this.sceneCreator);
  }

  initGame = () => {

    AxiosManager.get("/api/info", {})
      .then((response) => {
        LogManager.instance.debug(response);
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
        LogManager.instance.error(error);
      });
  };

  addTank(tankData, player) {
    this.tanksManager.createTank(tankData, player).then((SPS) => {
      if (tankData) {

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
              LogManager.instance.debug(tankData);
              LogManager.instance.debug(SPS.vars);
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
                    LogManager.instance.debug(response);
                })
                .catch(function (error) {
                    LogManager.instance.error(error);
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

  updateGameState(data) {
    for (let tankData of data.tanks) {
      const tank = this.SPSs[tankData.id];

      if (
        tank.mesh.position.z !== tankData.y ||
        tank.mesh.position.x !== tankData.x ||
        tank.mesh.rotation.y !== -tankData.angle
      ) {

        this.tanksManager.updateTankPosition(tank, tankData);
        tank.setParticles();
      }
    }

    for (let bullet of data.bullets){
        const capsule = new BABYLON.MeshBuilder.CreateSphere("bullet", {diameter:0.3}, this.sceneCreator.scene);
        capsule.position.x = bullet.x;
        capsule.position.z = bullet.y;
        capsule.position.y = bullet.z;

        this.bullets.push(capsule);
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
        LogManager.instance.debug(response);
      })
      .catch(function (error) {
        LogManager.instance.error(error);
      });
  }
}
