package pl.pastmo.robocker.engine.model;

import com.google.common.primitives.UnsignedInteger;
import pl.pastmo.robocker.engine.request.Move;

import java.util.LinkedList;
import java.util.List;

public class Tank implements MapItem, Containerized {

    private static Integer idCounter = 1;
    private Integer id;
    private Double x;
    private Double y;
    private Integer widthX = 5;
    private Integer widthY = 15;
    private Integer height = 10;
    private List<String> ips = new LinkedList<>();
    private Move destination;
    private static final double tankFast = 0.1;

    public Tank() {
        this.id = idCounter;
        idCounter++;
    }

    @Override
    public Double getX() {
        return x;
    }

    @Override
    public Double getY() {
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
    public MapItem setX(Double x) {
        this.x = x;
        return this;
    }

    @Override
    public MapItem setY(Double y) {
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

    public Move getDestination() {
        return destination;
    }

    public void updatePosition() {
        if (destination != null && (!x.equals(destination.getX()) || !y.equals(destination.getY()))) {

            double xDiff = destination.getX() - x;
            double yDiff = destination.getY() - y;

            double arc = 0.0;

            if (xDiff == 0.0 && yDiff == 0) {
                return;
            } else if (xDiff == 0.0 && yDiff > 0) {
                arc = Math.PI / 2;
            } else if (xDiff == 0.0 && yDiff < 0) {
                arc = -Math.PI / 2;
            } else {
                arc = Math.atan(yDiff / xDiff);
            }

            if (xDiff < 0) {
                arc = arc + Math.PI;
            }

            double changeX = Math.cos(arc) * tankFast;
            double changeY = Math.sin(arc) * tankFast;

            if (this.isExceededDestination(changeX, xDiff)) {
                x = destination.getX();
            } else {
                x += changeX;
            }
            if (this.isExceededDestination(changeY, yDiff)) {
                y = destination.getY();
            } else {
                y += changeY;
            }

            System.out.println("tank:" + id + " x:" + x + " y:" + y);
        }
    }

    private boolean isExceededDestination(double computedChange, double rawDiff) {
        return (computedChange > 0 && computedChange > rawDiff) || (computedChange < 0 && computedChange < rawDiff);
    }

    public void setDestination(Move destination) {
        this.destination = destination;
    }
}
