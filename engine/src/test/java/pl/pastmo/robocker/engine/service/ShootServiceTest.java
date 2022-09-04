package pl.pastmo.robocker.engine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pastmo.robocker.engine.model.ShootRequest;
import pl.pastmo.robocker.engine.model.Tank;
import pl.pastmo.robocker.engine.model.Turret;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ShootServiceTest {

    ShootService shootService;
    Tank tank;
    Turret turret;

    @BeforeEach
    void setUp() {
        shootService = new ShootService();

        turret = new Turret().setAngleVertical(Math.PI / 3).setAngleVertical(Math.PI / 4);

        tank = new Tank();
        tank.setX(148.0).setY(31.0).setWidthX(5).setWidthY(10).setHeight(5)
                .setTurret(turret);

    }

    @Test
    public void shootOnStart() {

        shootService.shootOnStart(tank);

        assertEquals(shootService.getShootRequests().size(), 1);

        ShootRequest shoot = shootService.getShootRequests().get(0);

        assertEquals(shoot.getTankId(), tank.getId());
        assertEquals(shoot.getTankAngle(), tank.getAngle());
        assertEquals(shoot.getTankX(), tank.getX());
        assertEquals(shoot.getTankY(), tank.getY());
        assertEquals(shoot.getTurretAngle(), tank.getTurret().getAngle());
        assertEquals(shoot.getTurretVerticalAngle(), tank.getTurret().getAngleVertical());

    }

    @Test
    public void shootOnEnd() {

        shootService.shootOnEnd(tank);

        assertEquals(shootService.getShootRequests().size(), 1);

        ShootRequest shoot = shootService.getShootRequests().get(0);

        assertEquals(shoot.getTankId(), tank.getId());
        assertEquals(shoot.getTankAngle(), tank.getAngle());
        assertEquals(shoot.getTankX(), tank.getX());
        assertEquals(shoot.getTankY(), tank.getY());
        assertEquals(shoot.getTurretAngle(), tank.getTurret().getAngle());
        assertEquals(shoot.getTurretVerticalAngle(), tank.getTurret().getAngleVertical());

    }
}
