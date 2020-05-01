package pl.mkjb.external.shared;

import static pl.mkjb.Settings.MAX_LINEAR_SPEED;
import static pl.mkjb.Settings.MIN_LINEAR_SPEED;

public class Velocity {
    public final int linear;

    public Velocity(int linear) {
        if (linear < MIN_LINEAR_SPEED || linear > MAX_LINEAR_SPEED) {
            throw new IllegalArgumentException("Linear velocity out of bounds");
        }

        this.linear = linear;
    }
}
