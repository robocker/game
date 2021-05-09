package pl.pastmo.robocker.engine.model;

import com.google.common.primitives.UnsignedInteger;

import java.util.LinkedList;
import java.util.List;

public class Player implements Containerized{
    private String imageName;
    private Integer id;
    private List<Tank> tanks = new LinkedList<Tank>();

    public Player(Integer id){

        this.id = id;
        this.imageName = "player";

    }

    public void addTank(Tank tank){
        this.tanks.add(tank);
    }

    public  List<Tank> gatTanks(){
        return tanks;
    }

    @Override
    public String getImageName() {
        return "robocker/player";
    }

    @Override
    public String getContainerName() {
        return "player_"+id;
    }

    @Override
    public UnsignedInteger getInsidePortNumber() {
        return UnsignedInteger.valueOf(3000);
    }

    @Override
    public boolean requiredExternalPort() {
        return true;
    }
}
