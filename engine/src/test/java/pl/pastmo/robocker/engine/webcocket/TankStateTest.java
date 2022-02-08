package pl.pastmo.robocker.engine.webcocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pastmo.robocker.engine.websocket.Tank;
import pl.pastmo.robocker.engine.websocket.TankState;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TankStateTest {

    @Test
    public void tankStateSerialization() throws JsonProcessingException {
        TankState tankState = new TankState();
        Tank tank = new Tank();
        tank.setX(13);
        tank.setY(42);
        tankState.add(tank);

        ObjectMapper mapper = new ObjectMapper();

        String result = mapper.writeValueAsString(tankState);

        assertEquals(result, "{\"tanks\":[{\"x\":13.0,\"y\":42.0}]}");
    }
}
