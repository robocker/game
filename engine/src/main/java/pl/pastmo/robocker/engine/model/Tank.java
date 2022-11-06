package pl.pastmo.robocker.engine.model;

import com.google.common.primitives.UnsignedInteger;
import pl.pastmo.robocker.engine.websocket.Action;
import pl.pastmo.robocker.engine.websocket.TankRequest;

import java.util.LinkedList;
import java.util.List;

public class Tank implements MapItem, Containerized {

    public static double DEFAULT_HEIGHT = 1.1;

    private static Integer idCounter = 1;
    private Integer id;
    private Double x;
    private Double y;
    private Double angle = 0d;
    private Turret turret;
    private Double widthX = 1.3;
    private Double widthYBack = 0.9;
    private Double widthYFront = 1.1;
    private Double height = DEFAULT_HEIGHT;
    private List<String> ips = new LinkedList<>();
    private LinkedList<Step> steps = new LinkedList<>();
    public static final double tankSpeed = 0.1;
    public static final double distanceTolerance = 0.001;
    public static final double rotationTolerance = Math.PI / 180;
    public static final double rotationSpeed = Math.PI / 24;

    public Tank() {
        this.id = idCounter;
        this.turret = new Turret();
        idCounter++;
    }

    static public void resetCounter() {
        idCounter = 1;
    }

    @Override
    public Double getX() {
        return x;
    }

    @Override
    public Double getY() {
        return y;
    }

    @Override
    public Tank setX(Double x) {
        this.x = x;
        return this;
    }

    @Override
    public Tank setY(Double y) {
        this.y = y;
        return this;
    }

    public Double getWidthX() {
        return widthX;
    }

    public Double getWidthYBack() {
        return widthYBack;
    }

    public Double getWidthYFront() {
        return widthYFront;
    }

    public Double getHeight() {
        return height;
    }

    public Tank setWidthX(Double widthX) {
        this.widthX = widthX;
        return this;
    }

    public Tank setWidthYBack(Double widthYBack) {
        this.widthYBack = widthYBack;
        return this;
    }

    public Tank setWidthYFront(Double widthYFront) {
        this.widthYFront = widthYFront;
        return this;
    }

    public Tank setHeight(Double height) {
        this.height = height;
        return this;
    }

    public Double getAngle() {
        return angle;
    }

    public Tank setAngle(Double angle) {
        this.angle = angle;
        return this;
    }

    public Turret getTurret() {
        return turret;
    }

    public Tank setTurret(Turret turret) {
        this.turret = turret;
        return this;
    }

    @Override
    public String getImageName() {
        return "robocker/tankbasic";
    }

    @Override
    public String getContainerName() {
        return "tank-" + id;
    }

    @Override
    public UnsignedInteger getInsidePortNumber() {
        return UnsignedInteger.valueOf(80);
    }

    @Override
    public void setExternalPort(UnsignedInteger port) {

    }

    @Override
    public void addIp(String ip) {
        ips.add(ip);
    }

    @Override
    public List<String> getIps() {
        return ips;
    }

    @Override
    public boolean requiredExternalPort() {
        return false;
    }

    public Integer getId() {
        return id;
    }

    public Tank setTurretVerticalAngle(double angle){
        this.getTurret().setAngleVertical(angle);
        return this;
    }

    public Tank setTurretAngle(double angle){
        this.getTurret().setAngle(angle);
        return this;
    }

    public static Integer getIdCounter(){
        return idCounter;
    }

    @Override
    public String toString() {
        return "Tank{" +
                "id=" + id +
                ", imageName=" + getImageName() +
                ", containerName=" + getContainerName() +
                '}';
    }

    public LinkedList<Step> getSteps() {
        return steps;
    }

    public void setTankRequest(TankRequest requests) {

        this.steps = new LinkedList<>();
        double stepAngle = angle;

        for (Action action : requests.getActions()) {

            StepTurret turretStep = turret.computeSteps(action.getTurret());
            turretStep.setAllSteps(this.steps);

            double newAngle = action.getAngle();
            double distance = action.getDistance();
            stepAngle += newAngle;

            if (newAngle != 0) {

                int howMany = (int) Math.abs(newAngle / rotationSpeed);

                if (howMany > 0) {

                    if (newAngle > 0) {
                        this.steps.add(turretStep.createNewStep().setAngle(rotationSpeed).setHowManyTimes(howMany));

                    } else {
                        this.steps.add(turretStep.createNewStep().setAngle(-rotationSpeed).setHowManyTimes(howMany));
                    }
                }

                double rest;
                if (newAngle > 0) {
                    rest = newAngle - (howMany * rotationSpeed);
                } else {
                    rest = newAngle - (howMany * (-rotationSpeed));
                }

                if (Math.abs(rest) > rotationTolerance) {
                    this.steps.add(turretStep.createNewStep().setAngle(rest).setHowManyTimes(1));
                }


            } else if (distance != 0) {
                double currentSpeed = tankSpeed;
                if (distance < 0) {
                    currentSpeed = -currentSpeed;
                }
                double changeX = Math.cos(stepAngle) * currentSpeed;
                double changeY = Math.sin(stepAngle) * currentSpeed;
                int howMany = (int) (distance / currentSpeed);

                this.steps.add(turretStep.createNewStep().setX(changeX).setY(changeY).setHowManyTimes(howMany));

                double rest = distance - (howMany * currentSpeed);
                if (Math.abs(rest) > distanceTolerance) {
                    this.steps.add(turretStep.createNewStep().setX(Math.cos(stepAngle) * rest).setY(Math.sin(stepAngle) * rest).setHowManyTimes(1));
                }
            }

            turretStep.updateShootTypeInCurrentAction();

        }
        System.out.println(steps);
    }


}
