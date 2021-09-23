import axios from "axios";
import { TanksManager } from "./TanksManager";

export class GameManager {
  SPSs = [];

  sceneCreator;
  tanksManager;

  constructor(sceneCreator) {
    this.sceneCreator = sceneCreator;
    this.tanksManager = new TanksManager(this.sceneCreator);
  }

  initGame(){

    axios.get('api/info', {})
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

  moveTank(index) {

    const tank = this.SPSs[index];

    tank.particles[1].rotation.y -= Math.PI / 180;
    tank.mesh.rotation.y += Math.PI / 180;
    tank.mesh.position.x += 0.1;

    tank.setParticles();


    axios.post('/api/tanks/move', {
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
