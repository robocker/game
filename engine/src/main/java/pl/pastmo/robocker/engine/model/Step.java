package pl.pastmo.robocker.engine.model;

public class Step extends StepTurret {
    public double x;
    public double y;
    public double angle;
    public int howManyTimes;


    public Step setX(double x) {
        this.x = x;
        return this;
    }

    public Step setY(double y) {
        this.y = y;
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

    @Override
    public String toString() {
        return "Step{" +
                "x=" + x +
                ", y=" + y +
                ", angle=" + angle +
                ", turretAngle=" + turretAngle +
                ", turretVerticalAngle=" + turretVerticalAngle +
                ", shootType=" + shootType +
                ", howManyTimes=" + howManyTimes +
                '}';
    }
}
