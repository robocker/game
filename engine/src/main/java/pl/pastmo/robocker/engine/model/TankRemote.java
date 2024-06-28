package pl.pastmo.robocker.engine.model;

public class TankRemote  extends AbstractTank implements MapItem{
    public static String REMOTE= "remote";

    @Override
    public TankRemote setX(Double x) {
        super.setX(x);
        return this;
    }

    @Override
    public TankRemote setZ(Double z) {
        super.setZ(z);
        return this;
    }

    @Override
    public TankRemote setWidthX(Double widthX) {
        super.setWidthX(widthX);
        return this;
    }

    @Override
    public TankRemote setWidthYBack(Double widthYBack) {
        super.setWidthYBack(widthYBack);
        return this;
    }

    @Override
    public TankRemote setWidthYFront(Double widthYFront) {
        super.setWidthYFront(widthYFront);
        return this;
    }

    @Override
    public TankRemote setHeight(Double height) {
        super.setHeight(height);
        return this;
    }

    @Override
    public TankRemote setAngle(Double angle) {
        super.setAngle(angle);
        return this;
    }

    @Override
    public TankRemote setTurret(Turret turret) {
        super.setTurret(turret);
        return this;
    }

    @Override
    public TankRemote setTurretAngle(double angle) {
        super.setTurretAngle(angle);
        return this;
    }

    @Override
    public TankRemote setTurretVerticalAngle(double angle) {
        super.setTurretVerticalAngle(angle);
        return this;
    }
}
