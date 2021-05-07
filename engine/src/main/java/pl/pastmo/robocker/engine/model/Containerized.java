package pl.pastmo.robocker.engine.model;

import com.google.common.primitives.UnsignedInteger;

public interface Containerized {
    String getImageName();
    String getContainerName();
    UnsignedInteger getInsidePortNumber();
    boolean requiredExternalPort();
}
