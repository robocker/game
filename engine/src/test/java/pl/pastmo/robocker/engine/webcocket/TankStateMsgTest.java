package pl.pastmo.robocker.engine.webcocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pastmo.robocker.engine.websocket.TankMsg;
import pl.pastmo.robocker.engine.websocket.TankStateMsg;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TankStateMsgTest {

    @Test
    public void tankStateSerialization() throws JsonProcessingException {
        TankStateMsg tankStateMsg = new TankStateMsg();
        TankMsg tank = new TankMsg();
        tank.setX(13);
        tank.setY(42);
        tank.setId(112);
        tankStateMsg.add(tank);

        ObjectMapper mapper = new ObjectMapper();

        String result = mapper.writeValueAsString(tankStateMsg);

        assertEquals(result, "{\"tanks\":[{\"x\":13.0,\"y\":42.0,\"id\":112}]}");
    }
}
