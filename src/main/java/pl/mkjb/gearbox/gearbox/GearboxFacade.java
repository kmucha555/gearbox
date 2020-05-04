package pl.mkjb.gearbox.gearbox;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GearboxFacade {
    public static GearboxDriver run() {
        return GearboxDriver.powerUpGearbox();
    }
}
