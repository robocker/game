package pl.pastmo.robocker.engine.model;

import com.google.common.primitives.UnsignedInteger;

import java.util.LinkedList;
import java.util.List;

public class Tank implements MapItem, Containerized {

    private static Integer idCounter = 1;
    private Integer id;
    private Integer x;
    private Integer y;
    private Integer widthX=5;
    private Integer widthY=15;
    private Integer height=10;
    private List<String> ips = new LinkedList<String>();

    public Tank() {
        this.id = idCounter;
        idCounter++;
    }

    @Override
    public Integer getX() {
        return x;
    }

    @Override
    public Integer getY() {
        return y;
    }

    @Override
    public Integer getWidthX() {
        return widthX;
    }

    @Override
    public Integer getWidthY() {
        return widthY;
    }

    @Override
    public Integer getHeight() {
        return height;
    }

    @Override
    public MapItem setX(Integer x) {
        this.x = x;
        return this;
    }

    @Override
    public MapItem setY(Integer y) {
        this.y = y;
        return this;
    }

    @Override
    public MapItem setWidthX(Integer widthX) {
        this.widthX = widthX;
        return this;
    }

    @Override
    public MapItem setWidthY(Integer widthY) {
        this.widthY = widthY;
        return this;
    }

    @Override
    public MapItem setHeight(Integer height) {
        this.height = height;
        return this;
    }

    @Override
    public String getImageName() {
        return "robocker/tankbasic";
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
