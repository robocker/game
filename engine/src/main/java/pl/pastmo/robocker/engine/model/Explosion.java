package pl.pastmo.robocker.engine.model;

public class Explosion {
    public static int MAX_TIMER = 42;
    private static Integer idCounter = 1;

    private Double x;
    private Double y;
    private Integer tankId;
    private Integer timer = 0;
    private Integer id;

    public Explosion() {
        this.id = idCounter;
        idCounter++;
    }

    public static Explosion fromBullet(Bullet bullet) {
        Explosion explosion = new Explosion();
        explosion.setX(bullet.getX());
        explosion.setY(bullet.getY());
        explosion.setTankId(bullet.getTankId());
        return explosion;
    }

    public Double getX() {
        return x;
    }

    public Explosion setX(Double x) {
        this.x = x;
        return this;
    }

    public Double getY() {
        return y;
    }

    public Explosion setY(Double y) {
        this.y = y;
        return this;
    }

    public int getTankId() {
        return tankId;
    }

    public Explosion setTankId(int tankId) {
        this.tankId = tankId;
        return this;
    }

    public int getTimer() {
        return timer;
    }

    public Explosion incrementTimer() {
        this.timer++;
        return this;
    }

    @Override
    public String toString() {
        return "Explosion{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
