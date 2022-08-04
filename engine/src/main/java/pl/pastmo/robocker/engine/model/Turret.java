package pl.pastmo.robocker.engine.model;

import pl.pastmo.robocker.engine.websocket.ActionTurret;

public class Turret {
    private Double angle = 0d;
    private Double angleVertical = 0d;

    public Double getAngle() {
        return angle;
    }

    public Turret setAngle(Double angle) {
        this.angle = angle;
        return this;
    }

    public Double getAngleVertical() {
        return angleVertical;
    }

    public Turret setAngleVertical(Double angleVertical) {
        this.angleVertical = angleVertical;
        return this;
    }

    public StepTurret computeSteps(ActionTurret actionTurret) {
        StepTurret step=  new StepTurret();

        step.setTurretAngle(actionTurret.getAngle());
        step.setTurretVerticalAngle(actionTurret.getVerticalAngle());
        step.setShootType(actionTurret.getShoot());

        return step;
    }
}
