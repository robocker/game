package pl.pastmo.robocker.engine.model;

import com.google.common.primitives.UnsignedInteger;

import java.util.List;

public interface Containerized {
    String getImageName();
    String getContainerName();
    UnsignedInteger getInsidePortNumber();
    void setExternalPort(UnsignedInteger port);
    boolean requiredExternalPort();
    void  addIp(String ip);
    List<String> getIps();
}
