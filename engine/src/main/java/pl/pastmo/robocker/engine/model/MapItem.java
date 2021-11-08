package pl.pastmo.robocker.engine.model;

public interface MapItem {
    Double getX();

    MapItem setX(Double x);

    Double getY();

    MapItem setY(Double y);

    Integer getWidthX();

    MapItem setWidthX(Integer widthX);

    Integer getWidthY();

    MapItem setWidthY(Integer widthY);

    Integer getHeight();

    MapItem setHeight(Integer height);
}
