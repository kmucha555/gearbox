package pl.mkjb.gearbox.gearbox;

import com.google.common.eventbus.Subscribe;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import pl.mkjb.gearbox.external.shared.BrakeThreshold;
import pl.mkjb.gearbox.external.shared.LinearSpeed;
import pl.mkjb.gearbox.external.shared.RevGauge;
import pl.mkjb.gearbox.external.shared.ThrottleThreshold;
import pl.mkjb.gearbox.gearbox.calculators.CalculatorFacade;
import pl.mkjb.gearbox.gearbox.shared.Gear;
import pl.mkjb.gearbox.gearbox.shared.VehicleStatusData;
import pl.mkjb.gearbox.settings.AggressiveMode;
import pl.mkjb.gearbox.settings.Mode;
import pl.mkjb.gearbox.settings.State;

import static pl.mkjb.gearbox.settings.AggressiveMode.SOFT;
import static pl.mkjb.gearbox.settings.Mode.COMFORT;
import static pl.mkjb.gearbox.settings.Setting.*;
import static pl.mkjb.gearbox.settings.State.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class GearboxDriver {
    private final Gearbox gearbox;
    private final AutomaticGearChangeCalculator automaticGearChangeCalculator;
    private final ManualGearChangeCalculator manualGearChangeCalculator;
    private final GearboxState gearboxState;
    private ThrottleThreshold throttleThreshold = new ThrottleThreshold(ZERO_THRESHOLD);
    private BrakeThreshold brakeThreshold = new BrakeThreshold(ZERO_THRESHOLD);
    private RevGauge revGauge = new RevGauge(IDLE_RPM);
    private LinearSpeed linearSpeed = new LinearSpeed(NO_SPEED);
    private State state = PARK;
    private Mode mode = COMFORT;
    private AggressiveMode aggressiveMode = SOFT;

    public static GearboxDriver powerUpGearbox(Gearbox gearbox) {
        val calculatorFacade = new CalculatorFacade();
        val automaticGearChangeCalc = new AutomaticGearChangeCalculator(calculatorFacade);
        val manualGearChangeCalc = new ManualGearChangeCalculator();
        val gearboxState = new GearboxState();

        return new GearboxDriver(gearbox, automaticGearChangeCalc, manualGearChangeCalc, gearboxState);
    }

    @Subscribe
    public void onEngineRevsChange(RevGauge revGauge) {
        this.revGauge = revGauge;
        automaticGearChange();
    }

    @Subscribe
    public void onGearStickPositionChange(State expectedState) {
        this.state = this.gearboxState.changeGearboxState().apply(expectedState, vehicleStatusData());

        if (this.state != MANUAL) {
            automaticGearChange();
        }
    }

    @Subscribe
    public void onDriveModeChange(Mode mode) {
        this.mode = mode;
        automaticGearChange();
    }

    @Subscribe
    public void onGearChangeMode(AggressiveMode aggressiveMode) {
        this.aggressiveMode = aggressiveMode;
        automaticGearChange();
    }

    @Subscribe
    public void onThrottleChange(ThrottleThreshold throttleThreshold) {
        this.throttleThreshold = throttleThreshold;
        automaticGearChange();
    }

    @Subscribe
    public void onBrakeApplied(BrakeThreshold brakeThreshold) {
        this.brakeThreshold = brakeThreshold;
        automaticGearChange();
    }

    @Subscribe
    public void onLinearSpeedChange(LinearSpeed linearSpeed) {
        this.linearSpeed = linearSpeed;
    }

    @Subscribe
    public void onPaddleUse(Gear gearChangeScope) {
        if (this.state == DRIVE) {
            this.state = MANUAL;
            val newGear = this.manualGearChangeCalculator.calculate().apply(gearChangeScope, vehicleStatusData());
            this.gearbox.changeGear(newGear);
        }
    }

    private void automaticGearChange() {
        val newGear = this.automaticGearChangeCalculator.calculate().apply(vehicleStatusData());
        this.gearbox.changeGear(newGear);
    }

    private VehicleStatusData vehicleStatusData() {
        return VehicleStatusData.builder()
                .throttleThreshold(throttleThreshold)
                .brakeThreshold(brakeThreshold)
                .revGauge(this.revGauge)
                .linearSpeed(this.linearSpeed)
                .state(this.state)
                .mode(this.mode)
                .aggressiveMode(this.aggressiveMode)
                .currentGear(checkGearboxGear())
                .build();
    }

    public State checkGearboxState() {
        return this.state;
    }

    public Gear checkGearboxGear() {
        return this.gearbox.currentGear();
    }
}
