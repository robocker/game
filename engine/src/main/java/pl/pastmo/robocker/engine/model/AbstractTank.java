package pl.pastmo.robocker.engine.model;

import pl.pastmo.robocker.engine.websocket.Action;
import pl.pastmo.robocker.engine.websocket.TankRequest;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractTank  implements MapItem {
    public static double DEFAULT_HEIGHT = 1.1;
    public static double SENSITIVE_DAMAGE_DISTANCE = 0.65;
    public static Integer START_LIFE_LEVEL = 3;
    public static final double tankSpeed = 0.1;
    public static final double distanceTolerance = 0.001;
    public static final double rotationTolerance = Math.PI / 180;
    public static final double rotationSpeed = Math.PI / 24;

    protected static Integer idCounter = 1;
    protected Integer id;
    protected Double x;
    protected Double z;
    protected Double angle = 0d;
    protected Turret turret;
    protected Double widthX = 1.3;
    protected Double widthYBack = 0.9;
    protected Double widthYFront = 1.1;
    protected Double height = DEFAULT_HEIGHT;
    protected List<String> ips = new LinkedList<>();
    protected LinkedList<Step> steps = new LinkedList<>();
    protected Integer lifeLevel = START_LIFE_LEVEL;

    static public void resetCounter() {
        idCounter = 1;
    }

    @Override
    public Double getX() {
        return x;
    }

    @Override
    public Double getZ() {
        return z;
    }

    @Override
    public AbstractTank setX(Double x) {
        this.x = x;
        return this;
    }

    @Override
    public AbstractTank setZ(Double z) {
        this.z = z;
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

    public Integer getId() {
        return id;
    }

    public AbstractTank setWidthX(Double widthX) {
        this.widthX = widthX;
        return this;
    }

    public AbstractTank setWidthYBack(Double widthYBack) {
        this.widthYBack = widthYBack;
        return this;
    }

    public AbstractTank setWidthYFront(Double widthYFront) {
        this.widthYFront = widthYFront;
        return this;
    }

    public AbstractTank setHeight(Double height) {
        this.height = height;
        return this;
    }

    public Double getAngle() {
        return angle;
    }

    public AbstractTank setAngle(Double angle) {
        this.angle = angle;
        return this;
    }

    public Turret getTurret() {
        return turret;
    }

    public AbstractTank setTurret(Turret turret) {
        this.turret = turret;
        return this;
    }

    public AbstractTank setTurretVerticalAngle(double angle) {
        this.getTurret().setAngleVertical(angle);
        return this;
    }

    public AbstractTank setTurretAngle(double angle) {
        this.getTurret().setAngle(angle);
        return this;
    }

    public AbstractTank setSteps(LinkedList<Step> steps) {
        this.steps = steps;
        return this;
    }

    public static Integer getIdCounter() {
        return idCounter;
    }

    public Integer getLifeLevel() {
        return lifeLevel;
    }

    public AbstractTank setLifeLevel(Integer lifeLevel) {
        this.lifeLevel = lifeLevel;
        return this;
    }

    public void decreaseLiveLevel(int hints) {
        lifeLevel -= hints;
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

                this.steps.add(turretStep.createNewStep().setX(changeX).setZ(changeY).setHowManyTimes(howMany));

                double rest = distance - (howMany * currentSpeed);
                if (Math.abs(rest) > distanceTolerance) {
                    this.steps.add(turretStep.createNewStep().setX(Math.cos(stepAngle) * rest).setZ(Math.sin(stepAngle) * rest).setHowManyTimes(1));
                }
            }

            turretStep.updateShootTypeInCurrentAction();

        }
    }

}
