package pl.pastmo.robocker.engine.websocket;

import java.util.LinkedList;
import java.util.List;

public class TankStateMsg {
    List<TankMsg> tanks = new LinkedList<>();

    public void add(TankMsg tank) {
        tanks.add(tank);
    }

    public List<TankMsg> getTanks() {
        return tanks;
    }
}
