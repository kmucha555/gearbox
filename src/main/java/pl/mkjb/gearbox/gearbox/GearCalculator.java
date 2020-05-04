package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import pl.mkjb.gearbox.gearbox.shared.Gear;
import pl.mkjb.gearbox.settings.Mode;
import pl.mkjb.gearbox.settings.State;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static pl.mkjb.gearbox.settings.DriveModeSetup.EcoSetup;
import static pl.mkjb.gearbox.settings.Mode.*;
import static pl.mkjb.gearbox.settings.Setting.*;
import static pl.mkjb.gearbox.settings.State.*;

class GearCalculator {
    private final Map<State, Function1<Mode, Function1<VehicleStatusData, Integer>>> gearboxStates;
    private final Map<Mode, Function1<VehicleStatusData, Integer>> driveModeCalculators;

    public GearCalculator() {
        this.driveModeCalculators = HashMap.of(
                ECO, ecoCalc(),
                COMFORT, comfortCalc(),
                SPORT, sportCalc(),
                SPORT_PLUS, sportPlusCalc()
        );

        this.gearboxStates = HashMap.of(
                DRIVE, selectDrive(),
                PARK, selectNeutral(),
                NEUTRAL, selectNeutral(),
                REVERSE, selectReverse()
        );
    }

    public Function1<VehicleStatusData, Gear> calculate() {
        return vehicleStatusData ->
                this.gearboxStates.get(vehicleStatusData.state)
                        .map(calc -> calc.apply(vehicleStatusData.mode))
                        .map((calc -> calc.apply(vehicleStatusData)))
                        .map(Gear::new)
                        .getOrElseThrow(() -> new IllegalArgumentException("Unknown gearbox state"));
    }

    private Function1<Mode, Function1<VehicleStatusData, Integer>> selectDrive() {
        return mode -> this.driveModeCalculators.get(mode)
                .getOrElseThrow(() -> new IllegalArgumentException("Unknown drive mode"));
    }

    private Function1<Mode, Function1<VehicleStatusData, Integer>> selectNeutral() {
        return mode -> neutralGear();
    }

    private Function1<Mode, Function1<VehicleStatusData, Integer>> selectReverse() {
        return mode -> reverseGear();
    }

    private Function1<VehicleStatusData, Integer> ecoCalc() {
        return vehicleStatusData -> {
            if (isThrottleApplied().test(vehicleStatusData, EcoSetup.throttleThreshold)) {
                if (isGearChangeRevs().test(vehicleStatusData, EcoSetup.downshiftRevs)) {
                    return newGear().apply(vehicleStatusData, DOWNSHIFT);
                }

                if (isGearChangeRevs().negate().test(vehicleStatusData, EcoSetup.upshiftRevs)) {
                    return newGear().apply(vehicleStatusData, UPSHIFT);
                }
            }

            return newGear().apply(vehicleStatusData, UPSHIFT);
        };
    }

    private BiPredicate<VehicleStatusData, Integer> isGearChangeRevs() {
        return (vehicleStatusData, gearChangeRevs) ->
                vehicleStatusData.revGauge.actualRevs <= gearChangeRevs;
    }


    private BiPredicate<VehicleStatusData, Integer> isThrottleApplied() {
        return (vehicleStatusData, throttleThreshold) -> getThrottleThreshold(vehicleStatusData) > throttleThreshold;
    }

    private int getThrottleThreshold(VehicleStatusData vehicleStatusData) {
        return vehicleStatusData.throttleThreshold.level;
    }

    private Predicate<VehicleStatusData> isVehicleBraking() {
        return vehicleStatusData -> getBrakeThreshold(vehicleStatusData) > ZERO_THRESHOLD;
    }

    private int getBrakeThreshold(VehicleStatusData vehicleStatusData) {
        return vehicleStatusData.brakeThreshold.level;
    }

    private Function1<VehicleStatusData, Integer> comfortCalc() {
        return vehicleStatusData -> 1;
    }

    private Function1<VehicleStatusData, Integer> sportCalc() {
        return vehicleStatusData -> 1;
    }

    private Function1<VehicleStatusData, Integer> sportPlusCalc() {
        return vehicleStatusData -> 1;
    }

    private Function1<VehicleStatusData, Integer> neutralGear() {
        return vehicleStatusData -> NEUTRAL_GEAR;
    }

    private Function1<VehicleStatusData, Integer> reverseGear() {
        return vehicleStatusData -> REVERSE_GEAR;
    }

    private Function2<VehicleStatusData, Integer, Integer> newGear() {
        return (vehicleStatusData, gearChangeScope) -> {
            final int newGear = vehicleStatusData.currentGear.gear + gearChangeScope;
            if (newGear < NEUTRAL_GEAR || newGear > MAX_GEAR_NUMBER) {
                return vehicleStatusData.currentGear.gear;
            }
            return newGear;
        };
    }
}
