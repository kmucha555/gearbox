package pl.mkjb.gearbox.external.shared;

import static pl.mkjb.gearbox.settings.Setting.MAX_THRESHOLD;
import static pl.mkjb.gearbox.settings.Setting.ZERO_THRESHOLD;

public class ThrottleThreshold implements Event {
    public final int level;

    public ThrottleThreshold(int level) {
        if (level < ZERO_THRESHOLD || level > MAX_THRESHOLD) {
            throw new IllegalArgumentException("Throttle threshold must be between 0 and 100");
        }

        this.level = level;
    }
}
