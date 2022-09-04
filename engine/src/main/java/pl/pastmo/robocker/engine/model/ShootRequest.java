package pl.pastmo.robocker.engine.model;

public class ShootRequest {
    private Integer tankId;
    private Double tankX;
    private Double tankY;
    private Double tankAngle;
    private Double turretAngle;
    private Double turretVerticalAngle;

    public static ShootRequest fromTank(Tank tank){
        return new ShootRequest()
                .setTankAngle(tank.getAngle())
                .setTankId(tank.getId())
                .setTankX(tank.getX())
                .setTankY(tank.getY())
                .setTurretAngle(tank.getTurret().getAngle())
                .setTurretVerticalAngle(tank.getTurret().getAngleVertical());
    }


    public Integer getTankId() {
        return tankId;
    }

    public ShootRequest setTankId(Integer tankId) {
        this.tankId = tankId;
        return this;
    }

    public Double getTankX() {
        return tankX;
    }

    public ShootRequest setTankX(Double tankX) {
        this.tankX = tankX;
        return this;
    }

    public Double getTankY() {
        return tankY;
    }

    public ShootRequest setTankY(Double tankY) {
        this.tankY = tankY;
        return this;
    }

    public Double getTankAngle() {
        return tankAngle;
    }

    public ShootRequest setTankAngle(Double tankAngle) {
        this.tankAngle = tankAngle;
        return this;
    }

    public Double getTurretAngle() {
        return turretAngle;
    }

    public ShootRequest setTurretAngle(Double turretAngle) {
        this.turretAngle = turretAngle;
        return this;
    }

    public Double getTurretVerticalAngle() {
        return turretVerticalAngle;
    }

    public ShootRequest setTurretVerticalAngle(Double turretVerticalAngle) {
        this.turretVerticalAngle = turretVerticalAngle;
        return this;
    }
}
