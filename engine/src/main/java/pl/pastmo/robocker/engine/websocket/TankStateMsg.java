package pl.pastmo.robocker.engine.websocket;

import pl.pastmo.robocker.engine.model.Bullet;
import pl.pastmo.robocker.engine.model.Explosion;

import java.util.LinkedList;
import java.util.List;

public class TankStateMsg {
    List<TankMsg> tanks = new LinkedList<>();
    List<Bullet> bullets = new LinkedList<>();
    List<Explosion> explosions = new LinkedList<>();


    public void add(TankMsg tank) {
        tanks.add(tank);
    }

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }

    public TankStateMsg setExplosions(List<Explosion> explosions) {
        this.explosions = explosions;
        return this;
    }

    public List<TankMsg> getTanks() {
        return tanks;
    }

    @Override
    public String toString() {
        return "TankStateMsg{" +
                "tanks=" + tanks +
                ", bullets=" + bullets +
                '}';
    }
}
