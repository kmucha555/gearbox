package pl.mkjb.gearbox.gearbox;

import com.google.common.eventbus.Subscribe;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import pl.mkjb.gearbox.external.shared.BrakeThreshold;
import pl.mkjb.gearbox.external.shared.ThrottleThreshold;
import pl.mkjb.gearbox.settings.GearboxState;
import pl.mkjb.gearbox.settings.Mode;

import static pl.mkjb.gearbox.settings.Setting.MAX_GEAR_NUMBER;
import static pl.mkjb.gearbox.settings.Setting.MIN_GEAR_NUMBER;

@Log4j2
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GearboxDriver {
    private final Gearbox gearbox;
    private final ExternalSystem externalSystem;
    private ThrottleThreshold throttleThreshold;
    private BrakeThreshold brakeThreshold;
    private GearboxState gearboxState;
    private Mode mode;

    public static GearboxDriver powerUpGearbox() {
        val externalSystems = new ExternalSystem();
        val gearbox = new Gearbox(MIN_GEAR_NUMBER, MAX_GEAR_NUMBER);

        return new GearboxDriver(gearbox, externalSystems);
    }

    @Subscribe
    public void onThrottleChange(ThrottleThreshold throttleThreshold) {
        this.throttleThreshold = throttleThreshold;
    }

    @Subscribe
    public void onBrakeApplied(BrakeThreshold brakeThreshold) {
        this.brakeThreshold = brakeThreshold;
    }

    @Subscribe
    public void onGearStickPositionChange(GearboxState gearboxState) {
        this.gearboxState = gearboxState;
    }

    @Subscribe
    public void onDriveModeChange(Mode mode) {
        this.mode = mode;
    }
}
