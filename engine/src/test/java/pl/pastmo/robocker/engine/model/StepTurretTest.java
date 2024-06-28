package pl.pastmo.robocker.engine.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pastmo.robocker.engine.websocket.ShootType;

import java.util.LinkedList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class StepTurretTest {

    @AfterEach
    void reset() {
        Tank.resetCounter();
    }

    @Test
    public void createNewStep_shoot_false() {
        StepTurret stepTurret = new StepTurret()
                .setTurretAngle(1.0)
                .setTurretVerticalAngle(3.0)
                .setShootType(ShootType.NOW);

        Step step = stepTurret.createNewStep();

        assertEquals(1.0, step.turretAngle);
        assertEquals(3.0, step.turretVerticalAngle);
        assertEquals(ShootType.FALSE, step.shootType);
    }

    @ParameterizedTest
    @MethodSource("shootTypes")
    public void updateShootTypeInCurrentAction(ShootType shootType, Integer index) {
        StepTurret stepTurret = new StepTurret()
                .setTurretAngle(1.0)
                .setTurretVerticalAngle(3.0)
                .setShootType(shootType);

        Step step1 = stepTurret.createNewStep();
        Step step2 = stepTurret.createNewStep();
        Step step3 = stepTurret.createNewStep();

        stepTurret.updateShootTypeInCurrentAction();

        Step[] steps = new Step[]{step1, step2, step3};
        for (int i = 0; i < steps.length; i++) {
            if (i == index) {
                assertEquals(shootType, steps[i].shootType);
            }
            else{
                assertEquals(ShootType.FALSE, steps[i].shootType);
            }
        }
    }

    static Stream<Arguments> shootTypes() {
        return Stream.of(
                Arguments.arguments(ShootType.NOW, 0),
                Arguments.arguments(ShootType.END_OF_ACTION, 2)
        );
    }

    @Test
    public void updateShootTypeInCurrentAction_without_body_move() {
        StepTurret stepTurret = new StepTurret()
                .setTurretAngle(1.0)
                .setTurretVerticalAngle(3.0)
                .setShootType(ShootType.NOW);

        LinkedList<Step> steps = new LinkedList<>();
        stepTurret.setAllSteps(steps);

        stepTurret.updateShootTypeInCurrentAction();

        assertEquals(1, steps.size());
        Step step = steps.get(0);

        assertEquals(1.0, step.turretAngle);
        assertEquals(3.0, step.turretVerticalAngle);
        assertEquals(ShootType.NOW, step.shootType);
    }


}
