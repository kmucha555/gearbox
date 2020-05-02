package pl.mkjb.gearbox.gearbox;

import lombok.val;

public class GearboxFacade {
    public static GearboxDriver run() {
        val externalSystems = new ExternalSystem();
        return GearboxDriver.powerUpGearbox(externalSystems);
    }
}
