package pl.mkjb.user.shared;

public class BrakeThreshold {
    public final int level;

    public BrakeThreshold(int level) {
        if (level < 0 || level > 100) {
            throw new IllegalArgumentException("Brake threshold must be between 0 and 100");
        }

        this.level = level;
    }
}
