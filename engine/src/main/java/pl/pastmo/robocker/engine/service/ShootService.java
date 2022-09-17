package pl.pastmo.robocker.engine.service;

import org.springframework.stereotype.Component;
import pl.pastmo.robocker.engine.model.Bullet;
import pl.pastmo.robocker.engine.model.Tank;

import java.util.LinkedList;

@Component("shootService")
public class ShootService {

    LinkedList<Bullet> bullets = new LinkedList<>();

    public void shootOnStart(Tank tank) {
        bullets.push(Bullet.fromTank(tank));
    }

    public void shootOnEnd(Tank tank) {
        bullets.push(Bullet.fromTank(tank));
    }

    public void processShoots() {
        for(Bullet bullet: bullets){
            double newX = bullet.getX() + Bullet.SPEED * Math.cos(bullet.getAngle());
            double newY = bullet.getY() + Bullet.SPEED * Math.sin(bullet.getAngle());

            bullet.setX(newX);
            bullet.setY(newY);
            bullet.setSpeed(Bullet.SPEED);
        }
    }

    public LinkedList<Bullet> getBullets() {
        return bullets;
    }


}
