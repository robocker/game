package pl.pastmo.robocker.engine.model;

import pl.pastmo.robocker.engine.websocket.ShootType;

public class Step extends StepTurret {
    public double x;
    public double z;
    public double angle;
    public int howManyTimes;


    public Step setX(double x) {
        this.x = x;
        return this;
    }

    public Step setZ(double z) {
        this.z = z;
        return this;
    }

    public Step setAngle(double angle) {
        this.angle = angle;
        return this;
    }

    public Step setHowManyTimes(int howManyTimes) {
        this.howManyTimes = howManyTimes;
        return this;
    }

    public Step setTurretAngle(double turretAngle) {
        this.turretAngle = turretAngle;
        return this;
    }

    public Step setTurretVerticalAngle(double turretVerticalAngle) {
        this.turretVerticalAngle = turretVerticalAngle;
        return this;
    }

    public Step setShootType(ShootType shootType) {
        this.shootType = shootType;
        return this;
    }

    @Override
    public String toString() {
        return "Step{" +
                "x=" + x +
                ", z=" + z +
                ", angle=" + angle +
                ", turretAngle=" + turretAngle +
                ", turretVerticalAngle=" + turretVerticalAngle +
                ", shootType=" + shootType +
                ", howManyTimes=" + howManyTimes +
                '}';
    }
}
