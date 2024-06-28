package pl.pastmo.robocker.engine.response;

import pl.pastmo.robocker.engine.model.AbstractTank;
import pl.pastmo.robocker.engine.model.Color;

import java.util.List;

public class PlayerInfo {
    public Integer id;
    public List<AbstractTank> tanks;
    public Color color;
    public boolean current = false;
    public boolean isRemote = false;

}
