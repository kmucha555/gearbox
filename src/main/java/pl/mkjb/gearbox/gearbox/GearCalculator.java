package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Function2;
import pl.mkjb.gearbox.gearbox.shared.Gear;
import pl.mkjb.gearbox.settings.Mode;
import pl.mkjb.gearbox.settings.State;

import java.util.function.Predicate;

import static io.vavr.API.*;
import static pl.mkjb.gearbox.settings.Mode.*;
import static pl.mkjb.gearbox.settings.Setting.*;
import static pl.mkjb.gearbox.settings.State.*;

class GearCalculator {
    private final Calculator ecoCalculator;
    private final Calculator comfortCalculator;
    private final Calculator sportCalculator;

    public GearCalculator() {
        this.ecoCalculator = new EcoCalculator();
        this.comfortCalculator = new ComfortCalculator();
        this.sportCalculator = new SportCalculator();
    }

    public Function1<VehicleStatusData, Gear> calculate() {
        return vehicleStatusData ->
                newGear().apply(vehicleStatusData, selectGearboxState()
                        .apply(vehicleStatusData.state)
                        .apply(vehicleStatusData.mode)
                        .apply(vehicleStatusData));
    }

    private Function2<VehicleStatusData, Integer, Gear> newGear() {
        return (vehicleStatusData, gearChangeScope) -> {
            var newGear = vehicleStatusData.currentGear.gear + gearChangeScope;

            if (gearChangeScope == KICKDOWN && isInDrive().test(vehicleStatusData) && isNotValidGear().test(newGear)) {
                newGear = vehicleStatusData.currentGear.gear + DOWNSHIFT;
            }

            if (isInDrive().test(vehicleStatusData) && isNotValidGear().test(newGear)) {
                return new Gear(vehicleStatusData.currentGear.gear);
            }

            return new Gear(newGear);
        };
    }

    private Function1<State, Function1<Mode, Function1<VehicleStatusData, Integer>>> selectGearboxState() {
        return state -> Match(state).of(
                Case($(DRIVE), drive()),
                Case($(PARK), park()),
                Case($(NEUTRAL), neutral()),
                Case($(REVERSE), reverse()));
    }

    private Function1<Mode, Function1<VehicleStatusData, Integer>> drive() {
        return mode -> Match(mode).of(
                Case($(ECO), ecoCalculator.calculate()),
                Case($(COMFORT), comfortCalculator.calculate()),
                Case($(SPORT), sportCalculator.calculate()));
    }

    private Function1<Mode, Function1<VehicleStatusData, Integer>> park() {
        return mode -> vehicleStatusData -> NEUTRAL_GEAR;
    }

    private Function1<Mode, Function1<VehicleStatusData, Integer>> neutral() {
        return mode -> vehicleStatusData -> NEUTRAL_GEAR;
    }

    private Function1<Mode, Function1<VehicleStatusData, Integer>> reverse() {
        return mode -> vehicleStatusData -> REVERSE_GEAR;
    }

    private Predicate<VehicleStatusData> isInDrive() {
        return vehicleStatusData -> vehicleStatusData.state.equals(DRIVE);
    }

    private Predicate<Integer> isNotValidGear() {
        return newGear -> newGear < FIRST_GEAR || newGear > MAX_GEAR_NUMBER;
    }
}
