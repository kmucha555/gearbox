package pl.mkjb.gearbox.gearbox.shared;

public class GearLimit {
    private final int max;

    public GearLimit(int max) {
        if (max < 6 || max > 10) {
            throw new IllegalArgumentException("Number of gears must be between 6 & 10");
        }
        
        this.max = max;
    }

    public int getMaxGearNumber() {
        return max;
    }
}
