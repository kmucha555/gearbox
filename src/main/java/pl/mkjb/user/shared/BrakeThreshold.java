package pl.mkjb.user.shared;

import static pl.mkjb.Settings.MAX_THRESHOLD;
import static pl.mkjb.Settings.MIN_THRESHOLD;

public class BrakeThreshold {
    public final int level;

    public BrakeThreshold(int level) {
        if (level < MIN_THRESHOLD || level > MAX_THRESHOLD) {
            throw new IllegalArgumentException("Brake threshold must be between 0 and 100");
        }

        this.level = level;
    }
}
