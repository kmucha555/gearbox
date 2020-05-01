package pl.mkjb.gearbox.gearbox.shared;

public class GearLimit {
    public final int maxGearNumber;

    public GearLimit(int maxGearNumber) {
        if (maxGearNumber < 6 || maxGearNumber > 10) {
            throw new IllegalArgumentException("Number of gears must be between 6 & 10");
        }
        
        this.maxGearNumber = maxGearNumber;
    }
}
