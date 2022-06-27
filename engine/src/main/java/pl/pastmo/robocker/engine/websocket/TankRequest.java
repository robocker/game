package pl.pastmo.robocker.engine.websocket;

import java.util.List;

public class TankRequest {

    Integer tankId;
    List<Action> actions;

    public Integer getTankId() {
        return tankId;
    }

    public TankRequest setTankId(Integer tankId) {
        this.tankId = tankId;
        return this;
    }

    public List<Action> getActions() {
        return actions;
    }

    public TankRequest setActions(List<Action> actions) {
        this.actions = actions;
        return this;
    }
}
