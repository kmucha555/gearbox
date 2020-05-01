package pl.mkjb.gearbox.gearbox;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.mkjb.user.shared.GearStickListener;
import pl.mkjb.user.shared.GearboxState;
import pl.mkjb.user.shared.ThrottleListener;
import pl.mkjb.user.shared.ThrottleThreshold;

import static pl.mkjb.user.shared.GearboxState.PARK;

@RequiredArgsConstructor
public class GearboxDriver implements ThrottleListener, GearStickListener {

    @NonNull
    private final GearboxFacade gearboxFacade;

    private GearboxState gearboxState = PARK;

    @Override
    public void onThrottleChange(ThrottleThreshold throttleThreshold) {
        System.out.println(throttleThreshold.level);
    }

    @Override
    public void onGearStickPositionChange(GearboxState gearboxState) {
        this.gearboxState = gearboxState;
    }
}
