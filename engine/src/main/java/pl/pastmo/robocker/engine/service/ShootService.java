package pl.pastmo.robocker.engine.service;

import org.springframework.stereotype.Component;
import pl.pastmo.robocker.engine.model.Bullet;
import pl.pastmo.robocker.engine.model.Explosion;
import pl.pastmo.robocker.engine.model.Tank;

import java.util.LinkedList;
import java.util.List;

@Component("shootService")
public class ShootService {

    LinkedList<Bullet> bullets = new LinkedList<>();
    LinkedList<Explosion> explosions = new LinkedList<>();

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

    public LinkedList<Bullet> getBullets() {
        return bullets;
    }

    public List<Explosion> getExplosions() {
        return explosions;
    }

    public int checkDemage(Tank tank, List<Explosion> explosions) {
        int result = 0;

        for (Explosion explosion : explosions) {
            double distance = Math.sqrt(Math.sqrt(explosion.getX() - tank.getX())
                    + Math.sqrt(explosion.getY() - tank.getY()));

            if (distance <= Tank.SENSITIVE_DAMAGE_DISTANCE) {
                result++;
            }
        }
        return result;
    }
}
