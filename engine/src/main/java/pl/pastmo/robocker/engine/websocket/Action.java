package pl.pastmo.robocker.engine.websocket;

public class Action {
    double angle = 0d;
    double distance = 0d;
    ActionTurret turret;

    public double getAngle() {
        return angle;
    }

    public Action setAngle(double angle) {
        this.angle = angle;
        return this;
    }

    public double getDistance() {
        return distance;
    }

    public Action setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public ActionTurret getTurret() {
        if(this.turret == null){
            this.turret = new ActionTurret();
        }
        return turret;
    }

    public Action setTurret(ActionTurret turret) {
        this.turret = turret;
        return this;
    }

    public Action setTurrentAngle(double angle) {
        this.getTurret().setAngle(angle);
        return this;
    }
    public Action setTurrentVerticalAngle(double angle) {
        this.getTurret().setVerticalAngle(angle);
        return this;
    }
}
