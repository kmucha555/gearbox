package pl.mkjb.gearbox.driver;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.gearbox.GearboxFacade;
import pl.mkjb.user.shared.GearStickListener;
import pl.mkjb.user.shared.GearboxState;
import pl.mkjb.user.shared.ThrottleListener;
import pl.mkjb.user.shared.ThrottleThreshold;

@RequiredArgsConstructor
public class GearboxDriver implements ThrottleListener, GearStickListener {

    @NonNull
    private final GearboxFacade gearboxFacade;

    private GearboxState gearboxState = GearboxState.PARK;

    @Override
    public void onThrottleChange(ThrottleThreshold throttleThreshold) {
        System.out.println(throttleThreshold.level);
    }

    @Override
    public void onGearStickPositionChange(GearboxState gearboxState) {
        this.gearboxState = gearboxState;
    }
}
