package pl.mkjb.gearbox;

import lombok.val;
import pl.mkjb.gearbox.driver.GearboxDriver;
import pl.mkjb.gearbox.external.Throttle;
import pl.mkjb.gearbox.external.shared.ThrottleThreshold;
import pl.mkjb.gearbox.gearbox.GearboxFacade;
import pl.mkjb.gearbox.gearbox.shared.GearLimit;

public class App {
    public static void main(String[] args) {
        val gearLimit = new GearLimit(8);
        val gearboxFacade = GearboxFacade.powerUpGearbox(gearLimit);

        val gearboxDriver = new GearboxDriver();

        val throttle = new Throttle();
        throttle.register(gearboxDriver);

        throttle.sendEvent(new ThrottleThreshold(50));
    }
}
