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
        for (Bullet bullet : bullets) {
            double newX = Bullet.SPEED * Math.cos(bullet.getAngle());
            double newY = Bullet.SPEED * Math.sin(bullet.getAngle());

            bullet.setGravitationSpeed(bullet.getGravitationSpeed() - Bullet.GRAVITY_ACCELERATION);

            double newZ = bullet.getZ() + bullet.getGravitationSpeed();

            if (newZ > 0) {

                bullet.setX(bullet.getX() + newX);
                bullet.setY(bullet.getY() + newY);
                bullet.setZ(newZ);
                bullet.setSpeed(Bullet.SPEED);

            } else {

                double proportion = bullet.getZ() / (bullet.getZ() - newZ);

                bullet.setX(bullet.getX() + newX * proportion);
                bullet.setY(bullet.getY() + newY * proportion);
                bullet.setZ(0.0);
                bullet.setSpeed(0.0);
                bullet.setGravitationSpeed(0.0);
                
                explode(bullet);
                

            }

        }
    }

    private void explode(Bullet bullet) {
        bullets.remove(bullet);
    }

    public LinkedList<Bullet> getBullets() {
        return bullets;
    }


}
