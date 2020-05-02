package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import pl.mkjb.gearbox.external.shared.BrakeThreshold;
import pl.mkjb.gearbox.external.shared.LinearSpeed;
import pl.mkjb.gearbox.settings.State;

import java.util.function.Predicate;

import static pl.mkjb.gearbox.settings.Setting.NO_SPEED;
import static pl.mkjb.gearbox.settings.Setting.ZERO_THRESHOLD;
import static pl.mkjb.gearbox.settings.State.*;

class GearboxState {
    private final ExternalSystem externalSystem;
    private final Map<State, Function1<DriverInput, State>> states;

    public GearboxState(ExternalSystem externalSystem) {

        this.externalSystem = externalSystem;
        this.states = HashMap.of(
                PARK, park(),
                REVERSE, reverse(),
                NEUTRAL, neutral(),
                DRIVE, drive()
        );
    }

    public Function2<State, DriverInput, State> change() {
        return (newState, driverInput) ->
                states.get(newState)
                        .map(tryChangeToNewState -> tryChangeToNewState.apply(driverInput))
                        .getOrElseThrow(() -> new IllegalArgumentException("Unknown gearbox state"));
    }

    private Function1<DriverInput, State> park() {
        return driverInput -> Option.of(driverInput.brakeThreshold)
                .filter(isBrakeForceApplied())
                .map(brakeThreshold -> externalSystem.getLinearSpeed())
                .filter(isVehicleStopped())
                .map(linearSpeed -> PARK)
                .getOrElseThrow(() -> new IllegalStateException("Can't change to park"));
    }

    private Function1<DriverInput, State> neutral() {
        return driverInput -> NEUTRAL;
    }

    private Function1<DriverInput, State> drive() {
        return driverInput -> Option.of(driverInput.brakeThreshold)
                .filter(isBrakeForceApplied())
                .map(brakeThreshold -> DRIVE)
                .getOrElseThrow(() -> new IllegalStateException("Can't change to drive"));
    }

    private Function1<DriverInput, State> reverse() {
        return driverInput -> Option.of(driverInput.brakeThreshold)
                .filter(isBrakeForceApplied())
                .map(brakeThreshold -> REVERSE)
                .getOrElseThrow(() -> new IllegalStateException("Can't change to reverse"));
    }

    private Predicate<BrakeThreshold> isBrakeForceApplied() {
        return brakeThreshold -> brakeThreshold.level > ZERO_THRESHOLD;
    }

    private Predicate<LinearSpeed> isVehicleStopped() {
        return linearSpeed -> linearSpeed.actualSpeed == NO_SPEED;
    }
}
