package pl.mkjb.gearbox;

import lombok.val;
import pl.mkjb.gearbox.driver.GearboxDriver;
import pl.mkjb.gearbox.gearbox.GearboxFacade;
import pl.mkjb.user.GearStick;
import pl.mkjb.user.Throttle;
import pl.mkjb.user.shared.GearboxState;
import pl.mkjb.user.shared.ThrottleThreshold;

public class App {
    public static void main(String[] args) {
        val gearboxFacade = GearboxFacade.powerUpGearbox(8);
        val gearboxDriver = new GearboxDriver(gearboxFacade);

        val throttle = new Throttle();
        throttle.register(gearboxDriver);
        throttle.sendEvent(new ThrottleThreshold(50));

        val gearStick = new GearStick();
        gearStick.register(gearboxDriver);
        gearStick.sendEvent(GearboxState.DRIVE);


    }
}
