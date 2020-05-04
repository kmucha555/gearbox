package pl.mkjb.gearbox.gearbox;

import com.google.common.eventbus.Subscribe;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import pl.mkjb.gearbox.external.shared.BrakeThreshold;
import pl.mkjb.gearbox.external.shared.LinearSpeed;
import pl.mkjb.gearbox.external.shared.RevGauge;
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
    private ThrottleThreshold throttleThreshold = new ThrottleThreshold(ZERO_THRESHOLD);
    private BrakeThreshold brakeThreshold = new BrakeThreshold(ZERO_THRESHOLD);
    private RevGauge revGauge = new RevGauge(IDLE_RPM);
    private LinearSpeed linearSpeed = new LinearSpeed(NO_SPEED);
    private State state = PARK;
    private Mode mode = COMFORT;

    public static GearboxDriver powerUpGearbox() {
        val gearboxCalc = new GearCalculator();
        val gearboxState = new GearboxState();
        val gearbox = new Gearbox(MIN_GEAR_NUMBER, MAX_GEAR_NUMBER);

        return new GearboxDriver(gearbox, gearboxCalc, gearboxState);
    }

    @Subscribe
    public void onEngineRevsChange(RevGauge revGauge) {
        this.revGauge = revGauge;
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
    public void onLinearSpeedChange(LinearSpeed linearSpeed) {
        this.linearSpeed = linearSpeed;
    }

    private void changeGear() {
        val gear = this.gearCalculator.calculate()
                .apply(vehicleStatusData());

        this.gearbox.changeGear(gear);
    }

    private VehicleStatusData vehicleStatusData() {
        return VehicleStatusData.builder()
                .throttleThreshold(throttleThreshold)
                .brakeThreshold(brakeThreshold)
                .revGauge(this.revGauge)
                .linearSpeed(this.linearSpeed)
                .state(this.state)
                .mode(this.mode)
                .currentGear(checkGearboxGear())
                .build();
    }

    public State checkGearboxState() {
        return this.state;
    }

    public Gear checkGearboxGear() {
        return this.gearbox.getCurrentGear();
    }
}
