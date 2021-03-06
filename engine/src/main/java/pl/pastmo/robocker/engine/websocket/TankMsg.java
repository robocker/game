package pl.pastmo.robocker.engine.websocket;

import pl.pastmo.robocker.engine.model.Player;
import pl.pastmo.robocker.engine.model.Tank;

public class TankMsg {
    double x;
    double y;
    Integer id;
    Integer playerId;
    Double angle;
    TurretMsg turret;

    public TankMsg() {
    }

    public TankMsg(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public static TankMsg fromTank(Tank tank, Player player){
        return new TankMsg(tank.getX(), tank.getY())
                .setId(tank.getId())
                .setAngle(tank.getAngle())
                .setPlayerId(player.getId())
                .setTurret(TurretMsg.fromTank(tank.getTurret()));

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

    public Integer getPlayerId() {
        return playerId;
    }

    public TankMsg setPlayerId(Integer playerId) {
        this.playerId = playerId;
        return this;
    }

    public Double getAngle() {
        return angle;
    }

    public TankMsg setAngle(Double angle) {
        this.angle = angle;
        return this;
    }

    public TankMsg setId(Integer id) {
        this.id = id;
        return this;
    }

    public TurretMsg getTurret() {
        return turret;
    }

    public TankMsg setTurret(TurretMsg turret) {
        this.turret = turret;
        return this;
    }



}
