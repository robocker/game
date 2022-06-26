package pl.pastmo.robocker.engine.webcocket;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pastmo.robocker.engine.websocket.TankRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TankRequestTest {

    @Test
    void parsing(){

        TankRequest request = new Gson().fromJson("{\"tankId\":3,\"actions\":[{\"angle\":-3.4371517359537083},{\"distance\":16.25117168296126}]}", TankRequest.class);

        assertEquals(3, request.getTankId());
        assertEquals(2,request.getActions().size());

        assertEquals(-3.4371517359537083, request.getActions().get(0).getAngle()) ;
        assertEquals(0, request.getActions().get(0).getDistance()) ;
        assertEquals(16.25117168296126, request.getActions().get(1).getDistance()) ;
        assertEquals(0, request.getActions().get(1).getAngle()) ;
    }
}
