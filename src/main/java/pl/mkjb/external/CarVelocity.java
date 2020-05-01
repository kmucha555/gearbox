package pl.mkjb.external;

import pl.mkjb.external.shared.Velocity;

import java.util.Random;

import static pl.mkjb.Settings.MAX_LINEAR_SPEED;
import static pl.mkjb.Settings.MIN_LINEAR_SPEED;

public class CarVelocity {
    private final Random random = new Random();

    public Velocity linear() {
        final int linearSpeed = random.nextInt((MAX_LINEAR_SPEED - MIN_LINEAR_SPEED) + MIN_LINEAR_SPEED);

        return new Velocity(linearSpeed);
    }
}
