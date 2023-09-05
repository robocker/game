package pl.pastmo.robocker.engine.webcocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pastmo.robocker.engine.exceptions.ConfigurationException;
import pl.pastmo.robocker.engine.model.Player;
import pl.pastmo.robocker.engine.model.Tank;
import pl.pastmo.robocker.engine.model.Turret;
import pl.pastmo.robocker.engine.websocket.TankMsg;
import pl.pastmo.robocker.engine.websocket.TankStateMsg;
import pl.pastmo.robocker.engine.websocket.TurretMsg;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TankStateMsgTest {

    @BeforeAll
    public static void cleanBefore() {
        Tank.resetCounter();
    }

    @AfterEach
    public void clean() {
        Tank.resetCounter();
    }

    @Test
    public void tankStateSerialization() throws JsonProcessingException {
        TankStateMsg tankStateMsg = new TankStateMsg();

        TankMsg tank = new TankMsg();
        tank.setX(13);
        tank.setZ(42);
        tank.setId(112);
        tank.setPlayerId(1034);
        tank.setAngle(0.1);
        tank.setTurret(new TurretMsg().setAngle(0.2).setAngleVertical(0.3));

        tankStateMsg.add(tank);

        ObjectMapper mapper = new ObjectMapper();

        String result = mapper.writeValueAsString(tankStateMsg);

        assertEquals("{\"tanks\":[{\"x\":13.0,\"z\":42.0,\"id\":112,\"playerId\":1034,\"angle\":0.1,\"lifeLevel\":3,\"turret\":{\"angle\":0.2,\"angleVertical\":0.3}}]}", result);
    }

    @Test
    public void fromTank() throws JsonProcessingException, ConfigurationException {
        Player player = new Player(3);

        player.addTank((new Tank()).setX(105.0).setZ(41.1).setAngle(0.3).setTurret((new Turret()).setAngle(0.5).setAngleVertical(0.6)));

        ObjectMapper mapper = new ObjectMapper();

        TankMsg tankStateMsg = TankMsg.fromTank(player.getTanks().get(0), player);

        String result = mapper.writeValueAsString(tankStateMsg);
        assertEquals("{\"x\":105.0,\"z\":41.1,\"id\":1,\"playerId\":3,\"angle\":0.3,\"lifeLevel\":3,\"turret\":{\"angle\":0.5,\"angleVertical\":0.6}}", result);
    }
}
