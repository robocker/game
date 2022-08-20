package pl.pastmo.robocker.engine.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pastmo.robocker.engine.websocket.ActionTurret;
import pl.pastmo.robocker.engine.websocket.ShootType;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TurretTest {

    @Test
    public void setAction() {

        ActionTurret action = (new ActionTurret())
                .setAngle(0.1)
                .setVerticalAngle(0.3)
                .setShoot(ShootType.NOW);
        Turret turret = new Turret();

        StepTurret result = turret.computeSteps(action);

        assertEquals(0.1, result.turretAngle, Tank.distanceTolerance);
        assertEquals(0.3, result.turretVerticalAngle, Tank.distanceTolerance);
        assertEquals(ShootType.NOW, result.shootType);

    }

    @Test
    public void updatePositionAngleSmallerThenStep() {

        Turret turret = new Turret();

        Step step = new Step().setTurretVerticalAngle(0.01).setTurretAngle(-0.01);
        turret.updatePosition(step, 1);

        assertEquals(-0.01, turret.getAngle());
        assertEquals(0.01, turret.getAngleVertical());

    }

    @Test
    public void updatePositionAngleSmallerThenStep_no_zero_start() {

        Turret turret = new Turret();
        turret.setAngle(0.3).setAngleVertical(0.4);

        Step step = new Step().setTurretAngle(-0.01).setTurretVerticalAngle(0.01);

        turret.updatePosition(step, 1);
        assertEquals(0.03820061220085058, turret.getAngle());
        assertEquals(0.13820061220085061, turret.getAngleVertical());
    }

    @Test
    public void updatePositionAngleSmallerThenStep_passing_zero() {

        Turret turret = new Turret();
        turret.setAngle(Math.PI * 2 - 0.1).setAngleVertical(0.12);

        Step step = new Step().setTurretAngle(0.01).setTurretVerticalAngle(-0.01);

        turret.updatePosition(step, 1);
        assertEquals(0.01, turret.getAngle(), 0.00001);
        assertEquals(-0.01, turret.getAngleVertical(), 0.00001);
    }

    @Test
    public void updatePositionAngleSmallerThenStep_big_angle_difference() {

        Turret turret = new Turret();
        turret.setAngle(Math.PI * 3 / 2)
                .setAngleVertical(Math.PI / 4);

        Step step = new Step()
                .setTurretAngle(Math.PI / 4)
                .setTurretVerticalAngle(Math.PI * 3 / 2);

        turret.updatePosition(step, 1);
        assertEquals(Math.PI * 3 / 2 + Turret.rotationSpeed, turret.getAngle(), 0.00001);
        assertEquals(Math.PI / 4 - Turret.rotationSpeed, turret.getAngleVertical(), 0.00001);
    }
}
