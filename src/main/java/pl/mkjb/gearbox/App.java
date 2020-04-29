package pl.mkjb.gearbox;

import lombok.val;
import pl.mkjb.gearbox.gearbox.GearboxFacade;
import pl.mkjb.gearbox.gearbox.shared.GearLimit;

public class App {
    public static void main(String[] args) {
        val gearLimit = new GearLimit(8);
        val gearbox = GearboxFacade.powerUpGearbox(gearLimit);

        val gearboxFacade = new GearboxFacade(gearbox);
    }
}
