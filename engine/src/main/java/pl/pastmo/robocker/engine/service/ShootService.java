package pl.pastmo.robocker.engine.service;

import org.springframework.stereotype.Component;
import pl.pastmo.robocker.engine.model.ShootRequest;
import pl.pastmo.robocker.engine.model.Tank;

import java.util.LinkedList;

@Component("shootService")
public class ShootService {

    LinkedList<ShootRequest> shootRequests = new LinkedList<>();

    public void shootOnStart(Tank tank) {
        shootRequests.push(ShootRequest.fromTank(tank));
    }

    public void shootOnEnd(Tank tank) {
        shootRequests.push(ShootRequest.fromTank(tank));
    }

    public void processShoots() {
    }


    public LinkedList<ShootRequest> getShootRequests() {
        return shootRequests;
    }

    public ShootService setShootRequests(LinkedList<ShootRequest> shootRequests) {
        this.shootRequests = shootRequests;
        return this;
    }

}
