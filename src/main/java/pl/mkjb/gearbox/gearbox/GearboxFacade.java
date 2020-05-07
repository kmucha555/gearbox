package pl.mkjb.gearbox.gearbox;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GearboxFacade {
    public static GearboxDriver run() {
        val gearbox = new Gearbox();
        return GearboxDriver.powerUpGearbox(gearbox);
    }
}
