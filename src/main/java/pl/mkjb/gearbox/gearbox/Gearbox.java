package pl.mkjb.gearbox.gearbox;

import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.gearbox.shared.Gear;
import pl.mkjb.gearbox.gearbox.shared.GearLimit;

@RequiredArgsConstructor
class Gearbox {
    private final GearLimit gearLimit;
    private int currentGear = 0;
    private static final int REVERSE_GEAR_LIMIT = -1;

    public void changeGear(Gear gear) {
        this.currentGear = gear.getGear();
    }

    public Gear getCurrentGear() {
        return new Gear(currentGear);
    }

    public GearLimit getGearLimit() {
        return gearLimit;
    }
}
