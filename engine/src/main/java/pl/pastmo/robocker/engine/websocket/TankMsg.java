package pl.pastmo.robocker.engine.websocket;

import pl.pastmo.robocker.engine.model.Tank;

public class TankMsg {
    double x;
    double y;
    Integer id;

    public TankMsg() {
    }

    public TankMsg(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static TankMsg fromTank(Tank tank){
        return new TankMsg(tank.getX(), tank.getY()).setId(tank.getId());
    }

    public double getX() {
        return x;
    }

    public TankMsg setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public TankMsg setY(double y) {
        this.y = y;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public TankMsg setId(Integer id) {
        this.id = id;
        return this;
    }
}
