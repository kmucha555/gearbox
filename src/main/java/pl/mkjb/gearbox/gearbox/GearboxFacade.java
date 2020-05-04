package pl.mkjb.gearbox.gearbox;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

import static pl.mkjb.gearbox.settings.Setting.MAX_GEAR_NUMBER;
import static pl.mkjb.gearbox.settings.Setting.MIN_GEAR_NUMBER;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GearboxFacade {
    public static GearboxDriver run() {
        val gearbox = new Gearbox(MIN_GEAR_NUMBER, MAX_GEAR_NUMBER);
        return GearboxDriver.powerUpGearbox(gearbox);
    }
}
