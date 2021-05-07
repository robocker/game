package pl.pastmo.robocker.engine.model;

import com.google.common.primitives.UnsignedInteger;

import java.util.List;

public class Player implements Containerized{
    String imageName;
    Integer id;
    List<Tank> tanks;

    public Player(Integer id){

        this.id = id;
        this.imageName = "player";

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
