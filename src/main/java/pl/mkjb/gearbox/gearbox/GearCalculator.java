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
        return statusData ->
                newGear().apply(statusData, selectGearboxState()
                        .apply(statusData.state)
                        .apply(statusData.mode)
                        .apply(statusData));
    }

    private Function2<VehicleStatusData, Integer, Gear> newGear() {
        return (statusData, gearChangeScope) -> {
            var newGear = statusData.currentGear.gear + gearChangeScope;

            if (gearChangeScope == KICKDOWN && isInDrive().test(statusData) && isNotValidGear().test(newGear)) {
                newGear = statusData.currentGear.gear + DOWNSHIFT;
            }

            if (isInDrive().test(statusData) && isNotValidGear().test(newGear)) {
                return new Gear(statusData.currentGear.gear);
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
        return mode -> statusData -> NEUTRAL_GEAR;
    }

    private Function1<Mode, Function1<VehicleStatusData, Integer>> neutral() {
        return mode -> statusData -> NEUTRAL_GEAR;
    }

    private Function1<Mode, Function1<VehicleStatusData, Integer>> reverse() {
        return mode -> statusData -> REVERSE_GEAR;
    }

    private Predicate<VehicleStatusData> isInDrive() {
        return statusData -> statusData.state.equals(DRIVE);
    }

    private Predicate<Integer> isNotValidGear() {
        return newGear -> newGear < FIRST_GEAR || newGear > MAX_GEAR_NUMBER;
    }
}
