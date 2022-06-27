package pl.pastmo.robocker.engine.websocket;

public class Action {
    double angle = 0d;
    double distance = 0d;

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
}
