package pl.mkjb.user.shared;

import static pl.mkjb.Settings.MAX_THRESHOLD;
import static pl.mkjb.Settings.MIN_THRESHOLD;

public class ThrottleThreshold {
    public final int level;

    public ThrottleThreshold(int level) {
        if (level < MIN_THRESHOLD || level > MAX_THRESHOLD) {
            throw new IllegalArgumentException("Throttle threshold must be between 0 and 100");
        }

        this.level = level;
    }
}
