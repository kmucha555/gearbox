package pl.mkjb.gearbox.gearbox.shared;

import static pl.mkjb.Settings.MAX_GEAR_NUMBER;
import static pl.mkjb.Settings.MIN_GEAR_NUMBER;

public class Gear {
    public final int newGear;

    public Gear(int gear) {
        if (gear < MIN_GEAR_NUMBER || gear > MAX_GEAR_NUMBER) {
            throw new IllegalArgumentException("Gear number out of bounds");
        }

        this.newGear = gear;
    }
}
