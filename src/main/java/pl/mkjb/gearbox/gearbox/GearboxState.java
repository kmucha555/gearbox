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

    public Function2<State, VehicleStatusData, Either<Try<IllegalStateException>, State>> changeGearboxState() {
        return (newState, vehicleStatusData) ->
                selectGearboxState().apply(newState)
                        .apply(vehicleStatusData);
    }

    private Function1<State, Function1<VehicleStatusData, Either<Try<IllegalStateException>, State>>> selectGearboxState() {
        return state -> Match(state).of(
                Case($(DRIVE), drive()),
                Case($(PARK), park()),
                Case($(NEUTRAL), neutral()),
                Case($(REVERSE), reverse()),
                Case($(MANUAL), manual()));
    }

    private Function1<VehicleStatusData, Either<Try<IllegalStateException>, State>> drive() {
        return statusData -> {
            if (isBrakeApplied().test(statusData)) {
                return Either.right(DRIVE);
            }

            return Either.left(Try.ofSupplier(() -> new IllegalStateException("Can't changeGearboxState to drive")));
        };
    }

    private Function1<VehicleStatusData, Either<Try<IllegalStateException>, State>> park() {
        return statusData -> {
            if (Predicates.allOf(isBrakeApplied(), isVehicleStopped()).test(statusData)) {
                return Either.right(PARK);
            }

            return Either.left(Try.ofSupplier(() -> new IllegalStateException("Can't changeGearboxState to park")));
        };
    }

    private Function1<VehicleStatusData, Either<Try<IllegalStateException>, State>> neutral() {
        return vehicleStatusData -> Either.right(NEUTRAL);
    }

    private Function1<VehicleStatusData, Either<Try<IllegalStateException>, State>> reverse() {
        return statusData -> {
            if (isBrakeApplied().test(statusData)) {
                return Either.right(REVERSE);
            }

            return Either.left(Try.ofSupplier(() -> new IllegalStateException("Can't changeGearboxState to reverse")));
        };
    }

    private Function1<VehicleStatusData, Either<Try<IllegalStateException>, State>> manual() {
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
