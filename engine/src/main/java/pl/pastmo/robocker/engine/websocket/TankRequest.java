package pl.pastmo.robocker.engine.websocket;

import java.util.List;

public class TankRequest {

    Integer tankId;
    List<Actions> actions;

    public Integer getTankId() {
        return tankId;
    }

    public void setTankId(Integer tankId) {
        this.tankId = tankId;
    }

    public List<Actions> getActions() {
        return actions;
    }

    public void setActions(List<Actions> actions) {
        this.actions = actions;
    }
}
