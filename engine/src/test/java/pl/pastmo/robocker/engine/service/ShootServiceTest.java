package pl.pastmo.robocker.engine.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pastmo.robocker.engine.model.Bullet;
import pl.pastmo.robocker.engine.model.Tank;
import pl.pastmo.robocker.engine.model.Turret;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ShootServiceTest {

    ShootService shootService;
    Tank tank;
    Turret turret;

    @AfterEach
    public void clean() {
        Tank.resetCounter();
    }

    @BeforeEach
    void setUp() {
        shootService = new ShootService();

        turret = new Turret().setAngleVertical(Math.PI / 3).setAngleVertical(Math.PI / 4);

        tank = new Tank();
        tank.setX(148.0).setY(31.0).setWidthX(5.0).setWidthYFront(8.0).setWidthYBack(8.0).setHeight(5.0)
                .setTurret(turret);

    }

    @Test
    public void shootOnStart() {

        shootService.shootOnStart(tank);

        assertEquals(shootService.getBullets().size(), 1);

        Bullet bullet = shootService.getBullets().get(0);

        assertEquals(bullet.getTankId(), tank.getId());
        assertEquals(bullet.getAngle(), tank.getAngle() + tank.getTurret().getAngle());
        assertEquals(bullet.getVerticalAngle(), tank.getTurret().getAngleVertical());
        assertEquals(bullet.getX(), tank.getX());
        assertEquals(bullet.getY(), tank.getY());
        assertEquals(bullet.getZ(), (double) tank.getHeight());
    }

    @Test
    public void shootOnEnd() {

        shootService.shootOnEnd(tank);

        assertEquals(shootService.getBullets().size(), 1);

        Bullet bullet = shootService.getBullets().get(0);

        assertEquals(bullet.getTankId(), tank.getId());
        assertEquals(bullet.getAngle(), tank.getAngle() + tank.getTurret().getAngle());
        assertEquals(bullet.getVerticalAngle(), tank.getTurret().getAngleVertical());
        assertEquals(bullet.getX(), tank.getX());
        assertEquals(bullet.getY(), tank.getY());
        assertEquals(bullet.getZ(), (double) tank.getHeight());
    }

    @ParameterizedTest
    @MethodSource("shootsProvider")
    public void processShoots(Bullet bullet, ExpectedResult expectedResult) {

        shootService.getBullets().add(bullet);

        assertEquals(shootService.getBullets().size(), 1);

        shootService.processShoots();

        assertEquals(expectedResult.angle, bullet.getAngle());
        assertEquals(expectedResult.verticalAngle, bullet.getVerticalAngle());
        assertEquals(expectedResult.x, bullet.getX(), 0.0001);
        assertEquals(expectedResult.y, bullet.getY(), 0.0001);
        assertEquals(expectedResult.z, bullet.getZ(), 0.0001);
        assertEquals(expectedResult.speed, bullet.getSpeed());
        assertEquals(expectedResult.gravitySpeed, bullet.getGravitationSpeed());

    }

    static Stream<Arguments> shootsProvider() {
        Double Z = 10.0;

        return Stream.of(
                Arguments.arguments(Bullet.fromTank(new Tank()
                                .setAngle(0.0)
                                .setTurretVerticalAngle(0.0)
                                .setX(0.0)
                                .setHeight(Z)
                                .setY(0.0)),
                        new ExpectedResult().setAngle(0).setVerticalAngle(0)
                                .setZ(Z - Bullet.GRAVITY_ACCELERATION)
                                .setX(Bullet.SPEED).setY(0)
                                .setGravitySpeed(-Bullet.GRAVITY_ACCELERATION)
                                .setSpeed(Bullet.SPEED)),
                Arguments.arguments(Bullet.fromTank(new Tank()
                                .setAngle(Math.PI / 2)
                                .setTurretVerticalAngle(Math.PI / 4)
                                .setX(0.0)
                                .setY(0.0)
                                .setHeight(Z)),
                        new ExpectedResult().setAngle(Math.PI / 2)
                                .setZ(Z + (Bullet.SPEED / Math.sqrt(2.0)) - Bullet.GRAVITY_ACCELERATION)
                                .setX(0)
                                .setY(Bullet.SPEED)
                                .setVerticalAngle(Math.PI / 4)
                                .setGravitySpeed((Bullet.SPEED / Math.sqrt(2.0)) - Bullet.GRAVITY_ACCELERATION)
                                .setSpeed(Bullet.SPEED)),
                Arguments.arguments(Bullet.fromTank(new Tank()
                                .setAngle(Math.PI)
                                .setTurretVerticalAngle(0.0)
                                .setX(0.0)
                                .setY(0.0)
                                .setHeight(Z)),
                        new ExpectedResult().setAngle(Math.PI)
                                .setZ(Z - Bullet.GRAVITY_ACCELERATION)
                                .setX(-Bullet.SPEED)
                                .setY(0)
                                .setGravitySpeed(-Bullet.GRAVITY_ACCELERATION)
                                .setSpeed(Bullet.SPEED)),
                Arguments.arguments(Bullet.fromTank(new Tank()
                                .setAngle(Math.PI * 3 / 2)
                                .setTurretVerticalAngle(-Math.PI / 4)
                                .setX(0.0)
                                .setY(0.0)
                                .setHeight(0.9)),
                        new ExpectedResult().setAngle(Math.PI * 3 / 2)
                                .setVerticalAngle(-Math.PI / 4)
                                .setZ(0)
                                .setX(0)
                                .setY(-1.115)
                                .setGravitySpeed(0)
                                .setSpeed(0)),
                Arguments.arguments(Bullet.fromTank(new Tank()
                                .setAngle(Math.PI / 4)
                                .setTurretVerticalAngle(Math.PI / 4)
                                .setTurretAngle(Math.PI / 4)
                                .setX(10.0)
                                .setY(3.0)
                                .setHeight(0.9)),
                        new ExpectedResult().setAngle(Math.PI / 2)
                                .setVerticalAngle(Math.PI / 4)
                                .setZ(6.971)
                                .setX(10.0)
                                .setY(13.0)
                                .setGravitySpeed(Bullet.SPEED/ Math.sqrt(2) - Bullet.GRAVITY_ACCELERATION)
                                .setSpeed(Bullet.SPEED))
        );
    }
}

class ExpectedResult {
    double x;
    double y;
    double z;
    double angle;
    double verticalAngle;
    double speed;
    double gravitySpeed;
    int tankId;

    public ExpectedResult setX(double x) {
        this.x = x;
        return this;
    }

    public ExpectedResult setY(double y) {
        this.y = y;
        return this;
    }

    public ExpectedResult setZ(double z) {
        this.z = z;
        return this;
    }

    public ExpectedResult setAngle(double angle) {
        this.angle = angle;
        return this;
    }

    public ExpectedResult setVerticalAngle(double verticalAngle) {
        this.verticalAngle = verticalAngle;
        return this;
    }


    public ExpectedResult setSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    public ExpectedResult setGravitySpeed(double gravitySpeed) {
        this.gravitySpeed = gravitySpeed;
        return this;
    }

    public ExpectedResult setTankId(int tankId) {
        this.tankId = tankId;
        return this;
    }
}
