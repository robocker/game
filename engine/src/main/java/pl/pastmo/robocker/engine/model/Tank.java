package pl.pastmo.robocker.engine.model;

import com.google.common.primitives.UnsignedInteger;

import java.util.List;

public class Tank extends AbstractTank implements MapItem, Containerized {


    protected String image = "robockergame/tankbasic";

    @Override
    public Tank setX(Double x) {
        super.setX(x);
        return this;
    }

    @Override
    public Tank setZ(Double z) {
        super.setZ(z);
        return this;
    }

    @Override
    public Tank setWidthX(Double widthX) {
        super.setWidthX(widthX);
        return this;
    }

    @Override
    public Tank setWidthYBack(Double widthYBack) {
        super.setWidthYBack(widthYBack);
        return this;
    }

    @Override
    public Tank setWidthYFront(Double widthYFront) {
        super.setWidthYFront(widthYFront);
        return this;
    }

    @Override
    public Tank setHeight(Double height) {
        super.setHeight(height);
        return this;
    }

    @Override
    public Tank setAngle(Double angle) {
        super.setAngle(angle);
        return this;
    }

    @Override
    public Tank setTurret(Turret turret) {
        super.setTurret(turret);
        return this;
    }

    @Override
    public Tank setTurretAngle(double angle) {
        super.setTurretAngle(angle);
        return this;
    }

    @Override
    public Tank setTurretVerticalAngle(double angle) {
        super.setTurretVerticalAngle(angle);
        return this;
    }


    public Tank() {
        this.id = idCounter;
        this.turret = new Turret();
        idCounter++;
    }

    public Tank(String imageName) {
        this();
        this.image = imageName;
    }

    @Override
    public String getImageName() {
        return image;
    }

    @Override
    public String getContainerName() {
        return "tank-" + id;
    }

    @Override
    public UnsignedInteger getInsidePortNumber() {
        return UnsignedInteger.valueOf(80);
    }

    @Override
    public void setExternalPort(UnsignedInteger port) {

    }

    @Override
    public void addIp(String ip) {
        ips.add(ip);
    }

    @Override
    public List<String> getIps() {
        return ips;
    }

    @Override
    public boolean requiredExternalPort() {
        return false;
    }




    @Override
    public String toString() {
        return "Tank{" +
                "id=" + id +
                ", imageName=" + getImageName() +
                ", containerName=" + getContainerName() +
                '}';
    }


}
