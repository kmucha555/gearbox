package pl.mkjb.gearbox;

import lombok.val;
import pl.mkjb.gearbox.gearbox.GearboxDriver;
import pl.mkjb.gearbox.gearbox.GearboxFacade;
import pl.mkjb.user.GearStick;
import pl.mkjb.user.Throttle;
import pl.mkjb.user.shared.ThrottleThreshold;

import static pl.mkjb.Settings.HALF_THRESHOLD;
import static pl.mkjb.user.shared.GearboxState.DRIVE;

public class App {
    public static void main(String[] args) {
        val gearboxFacade = GearboxFacade.powerUpGearbox();
        val gearboxDriver = new GearboxDriver(gearboxFacade);

        val throttle = new Throttle();
        throttle.register(gearboxDriver);
        throttle.sendEvent(new ThrottleThreshold(HALF_THRESHOLD));

        val gearStick = new GearStick();
        gearStick.register(gearboxDriver);
        gearStick.sendEvent(DRIVE);
    }
}
