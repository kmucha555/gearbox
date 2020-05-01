package pl.mkjb.gearbox.gearbox;

import com.google.common.eventbus.Subscribe;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import pl.mkjb.gearbox.external.shared.BrakeThreshold;
import pl.mkjb.gearbox.external.shared.ThrottleThreshold;
import pl.mkjb.gearbox.settings.State;
import pl.mkjb.gearbox.settings.Mode;

import static pl.mkjb.gearbox.settings.State.PARK;
import static pl.mkjb.gearbox.settings.Mode.COMFORT;
import static pl.mkjb.gearbox.settings.Setting.*;

@Log4j2
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GearboxDriver {
    private final Gearbox gearbox;
    private final GearCalculator gearCalculator;
    private final GearboxState gearboxState;
    private ThrottleThreshold throttleThreshold = new ThrottleThreshold(MIN_THRESHOLD);
    private BrakeThreshold brakeThreshold = new BrakeThreshold(MIN_THRESHOLD);
    private State state = PARK;
    private Mode mode = COMFORT;

    public static GearboxDriver powerUpGearbox() {
        val externalSystems = new ExternalSystem();
        val gearCalc = new GearCalculator(externalSystems);
        var gearState = new GearboxState(externalSystems);
        val gearbox = new Gearbox(MIN_GEAR_NUMBER, MAX_GEAR_NUMBER);

        return new GearboxDriver(gearbox, gearCalc, gearState);
    }

    private void changeGear() {
        val gear = gearCalculator.calculate()
                .apply(mode, driverInput());

        gearbox.changeGear(gear);
    }

    private DriverInput driverInput() {
        return DriverInput.builder()
                .throttleThreshold(throttleThreshold)
                .brakeThreshold(brakeThreshold)
                .state(state)
                .mode(mode)
                .build();
    }

    @Subscribe
    public void onThrottleChange(ThrottleThreshold throttleThreshold) {
        this.throttleThreshold = throttleThreshold;
        changeGear();
    }

    @Subscribe
    public void onBrakeApplied(BrakeThreshold brakeThreshold) {
        this.brakeThreshold = brakeThreshold;
        changeGear();
    }

    @Subscribe
    public void onGearStickPositionChange(State state) {
        this.state = state;
        changeGear();
    }

    @Subscribe
    public void onDriveModeChange(Mode mode) {
        this.mode = mode;
        changeGear();
    }
}
