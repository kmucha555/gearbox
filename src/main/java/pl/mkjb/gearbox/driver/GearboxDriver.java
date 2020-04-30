package pl.mkjb.gearbox.driver;

import pl.mkjb.gearbox.external.shared.Listener;
import pl.mkjb.gearbox.external.shared.ThrottleThreshold;

public class GearboxDriver implements Listener {
    @Override
    public void onThrottleChange(ThrottleThreshold throttleThreshold) {
        System.out.println(throttleThreshold.getLevel());
    }
}
