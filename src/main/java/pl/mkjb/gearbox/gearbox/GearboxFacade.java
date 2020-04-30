package pl.mkjb.gearbox.gearbox;

import lombok.RequiredArgsConstructor;
import lombok.val;
import pl.mkjb.gearbox.gearbox.shared.GearLimit;

@RequiredArgsConstructor
public class GearboxFacade {
    private final Gearbox gearbox;

    public static GearboxFacade powerUpGearbox(GearLimit gearLimit) {
        val gearbox = new Gearbox(gearLimit);
        return new GearboxFacade(gearbox);
    }
}
