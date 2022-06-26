package pl.pastmo.robocker.engine.model;

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
}
