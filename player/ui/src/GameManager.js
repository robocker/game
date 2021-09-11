import { TanksManager } from "./TanksManager";

export class GameManager {
  SPSs = [];

  sceneCreator;
  tanksManager;

  constructor(sceneCreator) {
    this.sceneCreator = sceneCreator;
    this.tanksManager = new TanksManager(this.sceneCreator);
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
  }
}
