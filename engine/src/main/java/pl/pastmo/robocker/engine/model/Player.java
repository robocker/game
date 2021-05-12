package pl.pastmo.robocker.engine.model;

import com.google.common.primitives.UnsignedInteger;

import java.util.LinkedList;
import java.util.List;

public class Player implements Containerized {

    private static Integer idCounter = 1;
    private String imageName;
    private Integer id;
    private List<Tank> tanks = new LinkedList<Tank>();
    private UnsignedInteger externalPort;

    public Player(Integer id) {
        if (id == null) {
            this.id = idCounter;
            idCounter++;
        } else {
            this.id = id;
        }

        this.imageName = "player";
    }

    public UnsignedInteger getExternalPort() {
        return externalPort;
    }


    public void addTank(Tank tank) {
        this.tanks.add(tank);
    }

    public List<Tank> gatTanks() {
        return tanks;
    }

    @Override
    public void setExternalPort(UnsignedInteger externalPort) {
        this.externalPort = externalPort;
    }

    @Override
    public String getImageName() {
        return "robocker/player";
    }

    @Override
    public String getContainerName() {
        return "player-" + id;
    }

    @Override
    public UnsignedInteger getInsidePortNumber() {
        return UnsignedInteger.valueOf(3000);
    }

    @Override
    public boolean requiredExternalPort() {
        return true;
    }

    @Override
    public String toString() {
        return "Player{" +
                "imageName='" + imageName + '\'' +
                ", id=" + id +
                ", imageName=" + getImageName() +
                ", containerName=" + getContainerName() +
                ", tanks=" + tanks +
                ", externalPort=" + externalPort +
                '}';
    }
}
