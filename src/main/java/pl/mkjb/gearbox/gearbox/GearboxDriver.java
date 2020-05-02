package pl.mkjb.gearbox.gearbox;

import com.google.common.eventbus.Subscribe;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import pl.mkjb.gearbox.external.shared.BrakeThreshold;
import pl.mkjb.gearbox.external.shared.ThrottleThreshold;
import pl.mkjb.gearbox.gearbox.shared.Gear;
import pl.mkjb.gearbox.settings.Mode;
import pl.mkjb.gearbox.settings.State;

import static pl.mkjb.gearbox.settings.Mode.COMFORT;
import static pl.mkjb.gearbox.settings.Setting.*;
import static pl.mkjb.gearbox.settings.State.PARK;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GearboxDriver {
    private final Gearbox gearbox;
    private final GearCalculator gearCalculator;
    private final GearboxState gearboxState;
    private final ExternalSystem externalSystem;
    private ThrottleThreshold throttleThreshold = new ThrottleThreshold(ZERO_THRESHOLD);
    private BrakeThreshold brakeThreshold = new BrakeThreshold(ZERO_THRESHOLD);
    private State state = PARK;
    private Mode mode = COMFORT;

    public static GearboxDriver powerUpGearbox(ExternalSystem externalSystems) {
        val gearboxCalc = new GearCalculator();
        val gearboxState = new GearboxState();
        val gearbox = new Gearbox(MIN_GEAR_NUMBER, MAX_GEAR_NUMBER);

        return new GearboxDriver(gearbox, gearboxCalc, gearboxState, externalSystems);
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
    public void onGearStickPositionChange(State expectedState) {
        this.state = this.gearboxState.change().apply(expectedState, vehicleStatusData());
        changeGear();
    }

    @Subscribe
    public void onDriveModeChange(Mode mode) {
        this.mode = mode;
        changeGear();
    }

    private void changeGear() {
        val gear = this.gearCalculator.calculate()
                .apply(vehicleStatusData());

        this.gearbox.changeGear(gear);
    }

    private VehicleStatusData vehicleStatusData() {
        return VehicleStatusData.builder()
                .throttleThreshold(this.throttleThreshold)
                .brakeThreshold(this.brakeThreshold)
                .state(this.state)
                .mode(this.mode)
                .externalSystem(this.externalSystem)
                .build();
    }

    public State checkGearboxState() {
        return this.state;
    }

    public Gear checkGearboxGear() {
        return this.gearbox.getCurrentGear();
    }
}
