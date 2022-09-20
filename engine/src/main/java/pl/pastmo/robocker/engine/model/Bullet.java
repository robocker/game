package pl.pastmo.robocker.engine.model;

public class Bullet {
    public static Double SPEED = 40.0;
    public static Double GRAVITY_ACCELERATION = 8.0;
    private Integer tankId;
    private Double x;
    private Double y;
    private Double z;
    private Double angle;
    private Double verticalAngle;
    private Double speed;
    private Double gravitationSpeed;

    public static Bullet fromTank(Tank tank) {
        return new Bullet()
                .setAngle(tank.getAngle())
                .setVerticalAngle(tank.getTurret().getAngleVertical())
                .setTankId(tank.getId())
                .setX(tank.getX())
                .setY(tank.getY())
                .setGravitationSpeed(0.0)
                .setZ((double) tank.getHeight());

    }

    public Integer getTankId() {
        return tankId;
    }

    public Bullet setTankId(Integer tankId) {
        this.tankId = tankId;
        return this;
    }

    public Double getX() {
        return x;
    }

    public Bullet setX(Double x) {
        this.x = x;
        return this;
    }

    public Double getY() {
        return y;
    }

    public Bullet setY(Double y) {
        this.y = y;
        return this;
    }

    public Double getZ() {
        return z;
    }

    public Bullet setZ(Double z) {
        this.z = z;
        return this;
    }

    public Double getAngle() {
        return angle;
    }

    public Bullet setAngle(Double angle) {
        this.angle = angle;
        return this;
    }

    public Double getVerticalAngle() {
        return verticalAngle;
    }

    public Bullet setVerticalAngle(Double verticalAngle) {
        this.verticalAngle = verticalAngle;
        return this;
    }

    public Double getSpeed() {
        return speed;
    }

    public Bullet setSpeed(Double speed) {
        this.speed = speed;
        return this;
    }

    public Double getGravitationSpeed() {
        return gravitationSpeed;
    }

    public Bullet setGravitationSpeed(Double gravitationSpeed) {
        this.gravitationSpeed = gravitationSpeed;
        return this;
    }
}
