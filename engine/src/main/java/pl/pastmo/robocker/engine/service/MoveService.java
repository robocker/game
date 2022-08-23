package pl.pastmo.robocker.engine.service;

import org.springframework.stereotype.Component;
import pl.pastmo.robocker.engine.model.Tank;

@Component("moveService")
public class MoveService {

    public void updatePosition(Tank tank){
        tank.updatePosition();
    }
}
