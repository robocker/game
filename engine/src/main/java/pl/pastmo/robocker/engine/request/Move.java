package pl.pastmo.robocker.engine.request;

public class Move{

    public Move(Double x, Double y){
        this.x = x;
        this.y = y;
    }

    private Double x;
    private Double y;

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}