package pl.pastmo.robocker.engine.model;

import pl.pastmo.robocker.engine.websocket.ActionTurret;
import pl.pastmo.robocker.engine.websocket.ShootType;

public class Turret {
    public static final double rotationSpeed = Math.PI / 12;
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
        StepTurret step = new StepTurret();

        step.setTurretAngle(actionTurret.getAngle());
        step.setTurretVerticalAngle(actionTurret.getVerticalAngle());
        step.setShootType(actionTurret.getShoot());

        return step;
    }

    public boolean updatePosition(Step step, int size) {

        if (angle != step.turretAngle) {
            if (angle > step.turretAngle) {
                angle -= rotationSpeed;
            } else if (angle < step.turretAngle) {
                angle += rotationSpeed;
            }
        }

        if (angleVertical != step.turretVerticalAngle) {
            if (angleVertical > step.turretVerticalAngle) {
                angleVertical -= rotationSpeed;
            } else if (angleVertical < step.turretVerticalAngle) {
                angleVertical += rotationSpeed;
            }
        }

        if ((angle != step.turretAngle || angleVertical != step.turretVerticalAngle) && step.shootType == ShootType.END_OF_ACTION) {
            return false;
        }
        return true;
    }
}
