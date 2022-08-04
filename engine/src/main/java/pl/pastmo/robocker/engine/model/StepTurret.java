package pl.pastmo.robocker.engine.model;

import pl.pastmo.robocker.engine.websocket.ShootType;

import java.util.LinkedList;

public class StepTurret {

    public double turretAngle;
    public double turretVerticalAngle;
    public ShootType shootType = ShootType.FALSE;
    private LinkedList<Step> steps = new LinkedList<Step>();

    public Step createNewStep() {
        Step step = new Step();
        step.setTurretAngle(this.turretAngle);
        step.setTurretVerticalAngle(this.turretVerticalAngle);

        this.steps.add(step);

        return step;
    }

    public void updateShootTypeInCurrentAction(){

        if(this.shootType == ShootType.NOW){
            this.steps.getFirst().setShootType(ShootType.NOW);
        }
        if(this.shootType == ShootType.END_OF_ACTION){
            this.steps.getLast().setShootType(ShootType.END_OF_ACTION);
        }

    }

    public StepTurret setTurretAngle(double turretAngle) {
        this.turretAngle = turretAngle;
        return this;
    }

    public StepTurret setTurretVerticalAngle(double turretVerticalAngle) {
        this.turretVerticalAngle = turretVerticalAngle;
        return this;
    }

    public StepTurret setShootType(ShootType shootType) {
        this.shootType = shootType;
        return this;
    }


}
