package pl.pastmo.robocker.engine.websocket;

public class ActionTurret {
    double angle = 0d;
    double verticalAngle = 0d;
    ShootType shoot = ShootType.FALSE;

    public double getAngle() {
        return angle;
    }

    public ActionTurret setAngle(double angle) {
        this.angle = angle;
        return this;
    }

    public double getVerticalAngle() {
        return verticalAngle;
    }

    public ActionTurret setVerticalAngle(double verticalAngle) {
        this.verticalAngle = verticalAngle;
        return this;
    }

    public ShootType getShoot() {
        return shoot;
    }

    public ActionTurret setShoot(ShootType shoot) {
        this.shoot = shoot;
        return this;
    }
}
