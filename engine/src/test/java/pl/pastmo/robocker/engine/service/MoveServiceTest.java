package pl.pastmo.robocker.engine.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pastmo.robocker.engine.model.Step;
import pl.pastmo.robocker.engine.model.Tank;
import pl.pastmo.robocker.engine.model.Turret;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;



@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class MoveServiceTest {

    @AfterEach
    public void clean() {
        Tank.resetCounter();
    }

    MoveService moveService = new MoveService();
    @Test
    public void updatePositionAngleSmallerThanStep() {

        Turret turret = new Turret();

        Step step = new Step().setTurretVerticalAngle(0.01).setTurretAngle(-0.01);
        moveService.updateTurretPosition(turret,step);

        assertEquals(-0.01, turret.getAngle());
        assertEquals(0.01, turret.getAngleVertical());

    }

    @Test
    public void updatePositionAngleSmallerThanStep_no_zero_start() {

        Turret turret = new Turret();
        turret.setAngle(0.3).setAngleVertical(0.4);

        Step step = new Step().setTurretAngle(-0.01).setTurretVerticalAngle(0.01);

        moveService.updateTurretPosition(turret,step);
        assertEquals(0.03820061220085058, turret.getAngle());
        assertEquals(0.13820061220085061, turret.getAngleVertical());
    }

    @Test
    public void updatePositionAngleSmallerThanStep_passing_zero() {

        Turret turret = new Turret();
        turret.setAngle(Math.PI * 2 - 0.1).setAngleVertical(0.12);

        Step step = new Step().setTurretAngle(0.01).setTurretVerticalAngle(-0.01);

        moveService.updateTurretPosition(turret,step);
        assertEquals(0.01, turret.getAngle(), 0.00001);
        assertEquals(-0.01, turret.getAngleVertical(), 0.00001);
    }

    @Test
    public void updatePositionAngleSmallerThanStep_big_angle_difference() {

        Turret turret = new Turret();
        turret.setAngle(Math.PI * 3 / 2)
                .setAngleVertical(Math.PI / 4);

        Step step = new Step()
                .setTurretAngle(Math.PI / 4)
                .setTurretVerticalAngle(Math.PI * 3 / 2);

        moveService.updateTurretPosition(turret,step);
        assertEquals(Math.PI * 3 / 2 + Turret.rotationSpeed, turret.getAngle(), 0.00001);
        assertEquals(Math.PI / 4 - Turret.rotationSpeed, turret.getAngleVertical(), 0.00001);
    }
}
