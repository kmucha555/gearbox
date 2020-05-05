package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.control.Option;
import pl.mkjb.gearbox.external.shared.BrakeThreshold;
import pl.mkjb.gearbox.external.shared.LinearSpeed;
import pl.mkjb.gearbox.gearbox.shared.VehicleStatusData;
import pl.mkjb.gearbox.settings.State;

import java.util.function.Predicate;

import static io.vavr.API.*;
import static pl.mkjb.gearbox.settings.Setting.NO_SPEED;
import static pl.mkjb.gearbox.settings.Setting.ZERO_THRESHOLD;
import static pl.mkjb.gearbox.settings.State.*;

final class GearboxState {

    public Function2<State, VehicleStatusData, State> changeGearboxState() {
        return (newState, vehicleStatusData) ->
                selectGearboxState()
                        .apply(newState)
                        .apply(vehicleStatusData);
    }

    private Function1<State, Function1<VehicleStatusData, State>> selectGearboxState() {
        return state -> Match(state).of(
                Case($(DRIVE), drive()),
                Case($(PARK), park()),
                Case($(NEUTRAL), neutral()),
                Case($(REVERSE), reverse()));
    }

    private Function1<VehicleStatusData, State> drive() {
        return vehicleStatusData -> Option.of(vehicleStatusData.brakeThreshold)
                .filter(isBrakeForceApplied())
                .map(brakeThreshold -> DRIVE)
                .getOrElseThrow(() -> new IllegalStateException("Can't changeGearboxState to drive"));
    }

    private Function1<VehicleStatusData, State> park() {
        return vehicleStatusData -> Option.of(vehicleStatusData.brakeThreshold)
                .filter(isBrakeForceApplied())
                .map(brakeThreshold -> vehicleStatusData.linearSpeed)
                .filter(isVehicleStopped())
                .map(linearSpeed -> PARK)
                .getOrElseThrow(() -> new IllegalStateException("Can't changeGearboxState to park"));
    }

    private Function1<VehicleStatusData, State> neutral() {
        return vehicleStatusData -> NEUTRAL;
    }

    private Function1<VehicleStatusData, State> reverse() {
        return vehicleStatusData -> Option.of(vehicleStatusData.brakeThreshold)
                .filter(isBrakeForceApplied())
                .map(brakeThreshold -> REVERSE)
                .getOrElseThrow(() -> new IllegalStateException("Can't changeGearboxState to reverse"));
    }

    private Predicate<BrakeThreshold> isBrakeForceApplied() {
        return brakeThreshold -> brakeThreshold.level > ZERO_THRESHOLD;
    }

    private Predicate<LinearSpeed> isVehicleStopped() {
        return linearSpeed -> linearSpeed.actualSpeed == NO_SPEED;
    }
}
