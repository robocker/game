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


}
