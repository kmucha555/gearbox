package pl.mkjb.gearbox.gearbox.shared;

public class GearLimit {
    private final int max;

    public GearLimit(int max) {
        if (max < 6 || max > 10) {
            throw new IllegalArgumentException("Minimal number of gears is between 6 & 10.");
        }
        this.max = max;
    }

    public int getMaxGearNumber() {
        return max;
    }
}
