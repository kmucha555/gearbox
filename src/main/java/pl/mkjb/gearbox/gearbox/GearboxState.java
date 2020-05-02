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
    private final Map<State, Function1<VehicleStatusData, State>> states;

    public GearboxState() {
        this.states = HashMap.of(
                PARK, park(),
                REVERSE, reverse(),
                NEUTRAL, neutral(),
                DRIVE, drive()
        );
    }

    public Function2<State, VehicleStatusData, State> change() {
        return (newState, vehicleStatusData) ->
                states.get(newState)
                        .map(tryChangeToNewState -> tryChangeToNewState.apply(vehicleStatusData))
                        .getOrElseThrow(() -> new IllegalArgumentException("Unknown gearbox state"));
    }

    private Function1<VehicleStatusData, State> park() {
        return vehicleStatusData -> Option.of(vehicleStatusData.brakeThreshold)
                .filter(isBrakeForceApplied())
                .map(brakeThreshold -> vehicleStatusData.externalSystem.getLinearSpeed())
                .filter(isVehicleStopped())
                .map(linearSpeed -> PARK)
                .getOrElseThrow(() -> new IllegalStateException("Can't change to park"));
    }

    private Function1<VehicleStatusData, State> neutral() {
        return vehicleStatusData -> NEUTRAL;
    }

    private Function1<VehicleStatusData, State> drive() {
        return vehicleStatusData -> Option.of(vehicleStatusData.brakeThreshold)
                .filter(isBrakeForceApplied())
                .map(brakeThreshold -> DRIVE)
                .getOrElseThrow(() -> new IllegalStateException("Can't change to drive"));
    }

    private Function1<VehicleStatusData, State> reverse() {
        return vehicleStatusData -> Option.of(vehicleStatusData.brakeThreshold)
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
