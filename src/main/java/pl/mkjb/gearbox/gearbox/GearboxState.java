package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Predicates;
import io.vavr.control.Either;
import io.vavr.control.Try;
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
                selectGearboxState().apply(newState)
                        .apply(vehicleStatusData);
    }

    private Function1<State, Function1<VehicleStatusData, State>> selectGearboxState() {
        return state -> Match(state).of(
                Case($(DRIVE), drive()),
                Case($(PARK), park()),
                Case($(NEUTRAL), neutral()),
                Case($(REVERSE), reverse()),
                Case($(MANUAL), manual()));
    }

    private Function1<VehicleStatusData, State> drive() {
        return statusData -> {
            if (isBrakeApplied().test(statusData)) {
                return DRIVE;
            }

            throw new IllegalStateException("Can't changeGearboxState to drive");
        };
    }

    private Function1<VehicleStatusData, State> park() {
        return statusData -> {
            if (Predicates.allOf(isBrakeApplied(), isVehicleStopped()).test(statusData)) {
                return PARK;
            }

            throw new IllegalStateException("Can't changeGearboxState to park");
        };
    }

    private Function1<VehicleStatusData, State> neutral() {
        return vehicleStatusData -> NEUTRAL;
    }

    private Function1<VehicleStatusData, State> reverse() {
        return statusData -> {
            if (isBrakeApplied().test(statusData)) {
                return REVERSE;
            }

            throw new IllegalStateException("Can't changeGearboxState to reverse");
        };
    }

    private Function1<VehicleStatusData, Either<Try, State>> manual() {
        return statusData -> {
            if (Predicates.allOf(isInDrive(), isVehicleStopped().negate()).test(statusData)) {
                return Either.right(MANUAL);
            }

            return Either.left(Try.ofSupplier(() -> new IllegalStateException("Can't changeGearboxState to manual")));
        };
    }

    private Predicate<VehicleStatusData> isBrakeApplied() {
        return statusData -> statusData.brakeThreshold.level > ZERO_THRESHOLD;
    }

    private Predicate<VehicleStatusData> isVehicleStopped() {
        return statusData -> statusData.linearSpeed.actualSpeed == NO_SPEED;
    }

    private Predicate<VehicleStatusData> isInDrive() {
        return statusData -> statusData.state.equals(DRIVE);
    }
}
