package pl.mkjb.gearbox.gearbox;

import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.gearbox.shared.Gear;

@RequiredArgsConstructor
class Gearbox {
    private int currentGear = 0;

    public void changeGear(Gear gear) {
        this.currentGear = gear.gear;
    }

    public Gear currentGear() {
        return new Gear(currentGear);
    }
}
