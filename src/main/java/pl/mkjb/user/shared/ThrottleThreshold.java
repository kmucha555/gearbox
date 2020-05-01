package pl.mkjb.user.shared;

public class ThrottleThreshold {
    public final int level;

    public ThrottleThreshold(int level) {
        if (level < 0 || level > 100) {
            throw new IllegalArgumentException("Throttle threshold must be between 0 and 100");
        }

        this.level = level;
    }
}
