package pl.pastmo.robocker.engine.service;

import org.springframework.stereotype.Component;
import pl.pastmo.robocker.engine.model.AbstractTank;
import pl.pastmo.robocker.engine.model.Bullet;
import pl.pastmo.robocker.engine.model.Explosion;
import pl.pastmo.robocker.engine.model.Tank;

import java.util.LinkedList;
import java.util.List;

@Component("shootService")
public class ShootService {

    LinkedList<Bullet> bullets = new LinkedList<>();
    LinkedList<Explosion> explosions = new LinkedList<>();

    public void shootOnStart(AbstractTank tank) {
        bullets.push(Bullet.fromTank(tank));
    }

    public void shootOnEnd(AbstractTank tank) {
        bullets.push(Bullet.fromTank(tank));
    }

    public void processShoots() {
        for (Bullet bullet : bullets) {
            double newX = Bullet.SPEED * Math.cos(bullet.getAngle());
            double newY = Bullet.SPEED * Math.sin(bullet.getAngle());

            bullet.setGravitationSpeed(bullet.getGravitationSpeed() - Bullet.GRAVITY_ACCELERATION);

            double newZ = bullet.getY() + bullet.getGravitationSpeed();

            if (newZ > 0) {

                bullet.setX(bullet.getX() + newX);
                bullet.setZ(bullet.getZ() + newY);
                bullet.setY(newZ);
                bullet.setSpeed(Bullet.SPEED);

            } else {

                double proportion = bullet.getY() / (bullet.getY() - newZ);

                bullet.setX(bullet.getX() + newX * proportion);
                bullet.setZ(bullet.getZ() + newY * proportion);
                bullet.setY(0.0);
                bullet.setSpeed(0.0);
                bullet.setGravitationSpeed(0.0);

                explode(bullet);


            }

        }

        for (Explosion explosion : explosions) {
            explosion.incrementTimer();

            if (explosion.getTimer() > Explosion.MAX_TIMER) {
                explosions.remove(explosion);
            }
        }
    }

    public void explode(Bullet bullet) {
        bullets.remove(bullet);
        explosions.push(Explosion.fromBullet(bullet));
    }

    public void removeExplosions(List<Explosion> explosions) {

        for (Explosion explosion : explosions) {
            explosions.remove(explosion);
        }
    }

    public LinkedList<Bullet> getBullets() {
        return bullets;
    }

    public List<Explosion> getExplosions() {
        return explosions;
    }

    public int checkDemage(AbstractTank tank, List<Explosion> explosions) {
        int result = 0;

        for (Explosion explosion : explosions) {
            double distance = Math.sqrt(Math.pow(explosion.getX() - tank.getX(), 2)
                    + Math.pow(explosion.getZ() - tank.getZ(), 2));

            if (distance <= Tank.SENSITIVE_DAMAGE_DISTANCE) {
                result++;
            }

            if (distance <= 5) {
            System.out.println("\ndistance:" + distance + " Tank:" + tank.getId() + "x:"
                    + tank.getX() + " y:" + tank.getZ() + " explosion:" + explosion);
            }
        }
        return result;
    }
}
