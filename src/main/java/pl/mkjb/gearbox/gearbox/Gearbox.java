package pl.mkjb.gearbox.gearbox;

import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.gearbox.shared.Gear;

@RequiredArgsConstructor
class Gearbox {
    private final int minGear;
    private final int maxGear;
    private int currentGear = 0;

    public void changeGear(Gear gear) {
        this.currentGear = gear.newGear;
    }

    public Gear getCurrentGear() {
        return new Gear(currentGear);
    }

}
