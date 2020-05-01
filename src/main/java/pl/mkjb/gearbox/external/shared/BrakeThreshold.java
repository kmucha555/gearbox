package pl.mkjb.gearbox.external.shared;

import static pl.mkjb.gearbox.settings.Setting.MAX_THRESHOLD;
import static pl.mkjb.gearbox.settings.Setting.MIN_THRESHOLD;

public class BrakeThreshold implements Event {
    public final int level;

    public BrakeThreshold(int level) {
        if (level < MIN_THRESHOLD || level > MAX_THRESHOLD) {
            throw new IllegalArgumentException("Brake threshold must be between 0 and 100");
        }

        this.level = level;
    }
}
