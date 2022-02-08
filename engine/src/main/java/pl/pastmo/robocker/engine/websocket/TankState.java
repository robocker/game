package pl.pastmo.robocker.engine.websocket;

import java.util.LinkedList;
import java.util.List;

public class TankState {
    List<Tank> tanks = new LinkedList<>();

    public void add(Tank tank) {
        tanks.add(tank);
    }

    public List<Tank> getTanks() {
        return tanks;
    }
}
