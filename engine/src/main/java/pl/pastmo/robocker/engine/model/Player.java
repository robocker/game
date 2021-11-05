package pl.pastmo.robocker.engine.model;

import com.google.common.primitives.UnsignedInteger;
import pl.pastmo.robocker.engine.exceptions.ConfigurationException;

import java.util.LinkedList;
import java.util.List;

public class Player implements Containerized {

    private String imageName;
    private Integer id;
    private List<Tank> tanks = new LinkedList<>();
    private UnsignedInteger externalPort;
    private List<String> ips = new LinkedList<>();

    public Player(Integer id) throws ConfigurationException {
        if (id == null) {
            throw new ConfigurationException("Player must have id");
        }

        this.id = id;
        this.imageName = "player";
    }

    public UnsignedInteger getExternalPort() {
        return externalPort;
    }


    public void addTank(Tank tank) {
        this.tanks.add(tank);
    }

    public List<Tank> getTanks() {
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
    public void addIp(String ip) {
        ips.add(ip);
    }

    @Override
    public List<String> getIps(){
        return this.ips;
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
