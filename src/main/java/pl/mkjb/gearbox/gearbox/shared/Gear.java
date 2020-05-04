package pl.mkjb.gearbox.gearbox.shared;

import lombok.EqualsAndHashCode;

import static pl.mkjb.gearbox.settings.Setting.MAX_GEAR_NUMBER;
import static pl.mkjb.gearbox.settings.Setting.MIN_GEAR_NUMBER;

@EqualsAndHashCode
public class Gear {
    public final int gear;

    public Gear(int gear) {
        if (gear < MIN_GEAR_NUMBER || gear > MAX_GEAR_NUMBER) {
            throw new IllegalArgumentException("Gear number out of bounds");
        }

        this.gear = gear;
    }
}
