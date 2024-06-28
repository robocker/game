package pl.pastmo.robocker.engine.model;

import com.google.common.primitives.UnsignedInteger;
import pl.pastmo.robocker.engine.exceptions.ConfigurationException;

import java.util.LinkedList;
import java.util.List;

public class Player implements Containerized {

    private String imageName;
    private Integer id;
    private List<AbstractTank> tanks = new LinkedList<>();
    private UnsignedInteger externalPort;
    private List<String> ips = new LinkedList<>();
    private Color color;
    private boolean isRemote= false;

    public Player(Integer id) throws ConfigurationException {

        this.id = id;
        this.imageName = "player";
    }

    public UnsignedInteger getExternalPort() {
        return externalPort;
    }


    public void addTank(AbstractTank tank) {
        this.tanks.add(tank);
    }

    public List<AbstractTank> getTanks() {
        return tanks;
    }

    public Color getColor() {
        return color;
    }

    public Player setColor(Color color) {
        this.color = color;
        return this;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public void setExternalPort(UnsignedInteger externalPort) {
        this.externalPort = externalPort;
    }

    @Override
    public String getImageName() {
        return "robockergame/player";
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

    public boolean isRemote() {
        return isRemote;
    }

    public Player setRemote(boolean remote) {
        isRemote = remote;
        return this;
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
