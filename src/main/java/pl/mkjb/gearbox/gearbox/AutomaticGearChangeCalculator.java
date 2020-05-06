package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Predicates;
import lombok.RequiredArgsConstructor;
import lombok.val;
import pl.mkjb.gearbox.gearbox.calculators.CalculatorFacade;
import pl.mkjb.gearbox.gearbox.shared.Gear;
import pl.mkjb.gearbox.gearbox.shared.VehicleStatusData;
import pl.mkjb.gearbox.settings.Mode;
import pl.mkjb.gearbox.settings.State;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static io.vavr.API.*;
import static pl.mkjb.gearbox.settings.Mode.*;
import static pl.mkjb.gearbox.settings.Setting.*;
import static pl.mkjb.gearbox.settings.State.*;

@RequiredArgsConstructor
class AutomaticGearChangeCalculator {
    private final CalculatorFacade calculatorFacade;

    public Function1<VehicleStatusData, Gear> calculate() {
        return statusData ->
                newGear().apply(
                        validateCalculatedGear().apply(statusData, selectGearboxState()
                                .apply(statusData.state)
                                .apply(statusData.mode)
                                .apply(statusData))
                );
    }

    private Function1<Integer, Gear> newGear() {
        return Gear::new;
    }

    private Function2<VehicleStatusData, Integer, Integer> validateCalculatedGear() {
        return (statusData, calculatedGearChangeScope) -> {
            val newGear = statusData.currentGear.gear + calculatedGearChangeScope;

            if (doubleKickdownIsNotPossible().test(calculatedGearChangeScope, statusData)) {
                val singleKickdown = statusData.currentGear.gear + DOWNSHIFT;

                if (singleKickdownIsNotPossible().test(singleKickdown)) {
                    return statusData.currentGear.gear;
                }

                return singleKickdown;
            }

            if (changeToNewGearIsNotPossible().test(newGear, statusData)) {
                return statusData.currentGear.gear;
            }

            return newGear;
        };
    }

    private BiPredicate<Integer, VehicleStatusData> doubleKickdownIsNotPossible() {
        return (gearChangeScope, statusData) -> {
            val newGear = statusData.currentGear.gear + gearChangeScope;
            return Predicates.is(isInDrive().test(statusData))
                    .and(Predicates.is(isDoubleKickdown().test(gearChangeScope)))
                    .and(Predicates.is(isInvalidGear().test(newGear)))
                    .test(true);
        };
    }

    private Predicate<Integer> singleKickdownIsNotPossible() {
        return gear -> isInvalidGear().test(gear);
    }

    private BiPredicate<Integer, VehicleStatusData> changeToNewGearIsNotPossible() {
        return (newGear, statusData) ->
                Predicates.is(isInDrive().test(statusData))
                        .and(Predicates.is(isInvalidGear().test(newGear)))
                        .test(true);
    }

    private Predicate<Integer> isDoubleKickdown() {
        return gearChangeScope -> gearChangeScope == KICKDOWN;
    }

    private Predicate<Integer> isInvalidGear() {
        return gear -> gear < FIRST_GEAR || gear > MAX_GEAR_NUMBER;
    }

    private Function1<State, Function1<Mode, Function1<VehicleStatusData, Integer>>> selectGearboxState() {
        return state -> Match(state).of(
                Case($(DRIVE), drive()),
                Case($(PARK), park()),
                Case($(NEUTRAL), neutral()),
                Case($(REVERSE), reverse()));
    }

    private Predicate<VehicleStatusData> isInDrive() {
        return statusData -> statusData.state.equals(DRIVE);
    }

    private Function1<Mode, Function1<VehicleStatusData, Integer>> drive() {
        return mode -> Match(mode).of(
                Case($(ECO), calculatorFacade.ecoCalculator()),
                Case($(COMFORT), calculatorFacade.comfortCalculator()),
                Case($(SPORT), calculatorFacade.sportCalculator()));
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
}
