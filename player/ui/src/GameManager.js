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

  initGame(){

    AxiosManager.get('api/info', {})
    .then(function (response) {
        console.log(response);
    })
    .catch(function (error) {
        console.log(error);
    });

  }

  addTank() {
    this.tanksManager.createTank()
    .then((SPS)=>{
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

    if(tryb == "prawo"){
        tank.mesh.position.z += 2;
    }
    if(tryb == "lewo"){
        tank.mesh.position.z -= 2;
    }
    else{
        tank.mesh.position.x += 2;
    }

    tank.setParticles();


    AxiosManager.post('/api/tanks/move', {
        ids: [42, 11],
        destination: {x: 33, y: 98}
    })
    .then(function (response) {
        console.log(response);
    })
    .catch(function (error) {
        console.log(error);
    });

  }
}
