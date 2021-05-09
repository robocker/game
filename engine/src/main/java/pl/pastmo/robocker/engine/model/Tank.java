package pl.pastmo.robocker.engine.model;

import com.google.common.primitives.UnsignedInteger;

public class Tank implements MapItem, Containerized{

    private static Integer idCounter = 1;
    private Integer id;

    public Tank(){
       this.id = idCounter;
       idCounter++;
    }

    @Override
    public String getImageName() {
        return "robocker/tankbasic";
    }

    @Override
    public String getContainerName() {
        return "tank-"+id;
    }

    @Override
    public UnsignedInteger getInsidePortNumber() {
        return UnsignedInteger.valueOf(80);
    }

    @Override
    public boolean requiredExternalPort() {
        return false;
    }
}
