package pl.mkjb.gearbox.gearbox;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import pl.mkjb.gearbox.gearbox.shared.GearLimit;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GearboxFacade {

    @NonNull
    private final Gearbox gearbox;

    public static GearboxFacade powerUpGearbox(int gearLimit) {
        val limit = new GearLimit(gearLimit);
        val gearbox = new Gearbox(limit);

        return new GearboxFacade(gearbox);
    }
}
