package pl.mkjb.gearbox.gearbox;

import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.gearbox.shared.GearLimit;

@RequiredArgsConstructor
public class GearboxFacade {
    private final Gearbox gearbox;

    public static Gearbox powerUpGearbox(GearLimit gearLimit) {
        return new Gearbox(gearLimit);
    }
}
