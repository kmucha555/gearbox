package pl.mkjb.gearbox.gearbox;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;

import static pl.mkjb.Settings.MAX_GEAR_NUMBER;
import static pl.mkjb.Settings.MIN_GEAR_NUMBER;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GearboxFacade {

    @NonNull
    private final Gearbox gearbox;

    public static GearboxFacade powerUpGearbox() {
        val gearbox = new Gearbox(MIN_GEAR_NUMBER, MAX_GEAR_NUMBER);

        return new GearboxFacade(gearbox);
    }
}
