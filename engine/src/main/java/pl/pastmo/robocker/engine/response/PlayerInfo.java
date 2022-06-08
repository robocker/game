package pl.pastmo.robocker.engine.response;

import pl.pastmo.robocker.engine.model.Color;
import pl.pastmo.robocker.engine.model.Tank;

import java.util.List;

public class PlayerInfo {
    public List<Tank> tanks;
    public Color color;
    public boolean current = false;

}
