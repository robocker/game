package pl.pastmo.robocker.engine.websocket;

import pl.pastmo.robocker.engine.model.Turret;

public class TurretMsg {
    Double angle;
    Double angleVertical;

    public static TurretMsg fromTank(Turret turret){
        return (new TurretMsg()).setAngle(turret.getAngle()).setAngleVertical(turret.getAngleVertical());
    }

    public Double getAngle() {
        return angle;
    }

    public TurretMsg setAngle(Double angle) {
        this.angle = angle;
        return this;
    }

    public Double getAngleVertical() {
        return angleVertical;
    }

    public TurretMsg setAngleVertical(Double angleVertical) {
        this.angleVertical = angleVertical;
        return this;
    }

}
