package pl.pastmo.robocker.engine.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.pastmo.robocker.engine.model.Step;
import pl.pastmo.robocker.engine.model.Tank;
import pl.pastmo.robocker.engine.model.Turret;
import pl.pastmo.robocker.engine.websocket.ShootType;

@Component("moveService")
public class MoveService {

    @Autowired
    private ShootService shootService;

    public void updatePosition(Tank tank) {
        for (Step step : tank.getSteps()) {

            if (step.shootType == ShootType.NOW) {
                this.shootService.shootOnStart(tank);
                step.shootType = ShootType.FALSE;
            }

            if (step.howManyTimes > 0) {
                tank.setX(tank.getX() + step.x);
                tank.setY(tank.getY() + step.y);
                tank.setAngle(tank.getAngle() + step.angle);
                step.howManyTimes--;

                if (tank.getAngle() >= Math.PI * 2) {
                    tank.setAngle(tank.getAngle() - Math.PI * 2);
                }
                if (tank.getAngle() < 0) {
                    tank.setAngle(tank.getAngle() + Math.PI * 2);
                }

            }

            boolean turretDone = updateTurretPosition(tank.getTurret(), step);

            if (step.howManyTimes == 0 && (turretDone || (tank.getSteps().size() > 1 && step.shootType != ShootType.END_OF_ACTION))) {
                tank.getSteps().remove(step);
                if (step.shootType == ShootType.END_OF_ACTION) {
                    this.shootService.shootOnEnd(tank);
                }
            }
            break;
        }
    }

    public boolean updateTurretPosition(Turret turret, Step step) {


        if (turret.getAngle() != step.turretAngle) {
            turret.setAngle(computeAngleValue(step.turretAngle, turret.getAngle()));
        }

        if (turret.getAngleVertical() != step.turretVerticalAngle) {
            turret.setAngleVertical(computeAngleValue(step.turretVerticalAngle, turret.getAngleVertical()));
        }

        if (turret.getAngle() != step.turretAngle || turret.getAngleVertical() != step.turretVerticalAngle) {
            return false;
        }
        return true;

    }

    private double computeAngleValue(double requiredAngle, double currentAngle) {

        double sign = currentAngle > requiredAngle ? -1 : 1;
        double absRequiredAngle = Math.abs(requiredAngle - currentAngle);

        if (absRequiredAngle > Math.PI) {
            sign *= -1;
            absRequiredAngle = Math.abs(Math.PI * 2 - absRequiredAngle);
        }

        double result = 0;

        if (absRequiredAngle > Turret.rotationSpeed) {
            result = currentAngle + sign * Turret.rotationSpeed;
        } else {
            result = currentAngle + sign * absRequiredAngle;
        }

        if (result >= Math.PI * 2) {
            result -= Math.PI * 2;
        }
        return result;

    }

    public void setShootService(ShootService shootService) {
        this.shootService = shootService;
    }
}
