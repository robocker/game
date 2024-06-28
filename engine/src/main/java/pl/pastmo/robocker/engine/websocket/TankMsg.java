package pl.pastmo.robocker.engine.websocket;

import pl.pastmo.robocker.engine.model.AbstractTank;
import pl.pastmo.robocker.engine.model.Player;
import pl.pastmo.robocker.engine.model.Tank;

public class TankMsg {
    double x;
    double z;
    Integer id;
    Integer playerId;
    Double angle;
    Integer lifeLevel;
    TurretMsg turret;

    public TankMsg() {
        this.lifeLevel = Tank.START_LIFE_LEVEL;
    }

    public TankMsg(double x, double z) {
        this.x = x;
        this.z = z;
    }


    public static TankMsg fromTank(AbstractTank tank, Player player){
        return new TankMsg(tank.getX(), tank.getZ())
                .setId(tank.getId())
                .setAngle(tank.getAngle())
                .setLifeLevel(tank.getLifeLevel())
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

    public double getZ() {
        return z;
    }

    public TankMsg setZ(double z) {
        this.z = z;
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

    public Integer getLifeLevel() {
        return lifeLevel;
    }

    public TankMsg setLifeLevel(Integer lifeLevel) {
        this.lifeLevel = lifeLevel;
        return this;
    }

    public TurretMsg getTurret() {
        return turret;
    }

    public TankMsg setTurret(TurretMsg turret) {
        this.turret = turret;
        return this;
    }

    @Override
    public String toString() {
        return "TankMsg{" +
                "x=" + x +
                ", z=" + z +
                ", id=" + id +
                ", playerId=" + playerId +
                ", angle=" + angle +
                ", turret=" + turret +
                '}';
    }
}
