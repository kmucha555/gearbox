package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Function2;
import lombok.RequiredArgsConstructor;
import lombok.val;
import pl.mkjb.gearbox.gearbox.calculators.CalculatorFacade;
import pl.mkjb.gearbox.gearbox.shared.Gear;
import pl.mkjb.gearbox.gearbox.shared.VehicleStatusData;
import pl.mkjb.gearbox.settings.Mode;
import pl.mkjb.gearbox.settings.State;

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

    //TODO refactor this method
    private Function2<VehicleStatusData, Integer, Integer> validateCalculatedGear() {
        return (statusData, gearChangeScope) -> {
            val currentGear = statusData.currentGear.gear;
            var newGear = currentGear + gearChangeScope;

            if (isInDrive().test(statusData)) {
                if (isDoubleKickdown().test(gearChangeScope) && isValidGear().negate().test(newGear)) {
                    newGear = currentGear + DOWNSHIFT;
                    if (isValidGear().negate().test(newGear)) {
                        return currentGear;
                    }
                    return newGear;
                }

                if (isValidGear().negate().test(newGear)) {
                    return currentGear;
                }
            }

            return newGear;
        };
    }

    private Predicate<Integer> isDoubleKickdown() {
        return gearChangeScope -> gearChangeScope == KICKDOWN;
    }

    private Predicate<Integer> isValidGear() {
        return gear -> gear >= FIRST_GEAR && gear <= MAX_GEAR_NUMBER;
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

    private Predicate<VehicleStatusData> isInDrive() {
        return statusData -> statusData.state.equals(DRIVE);
    }
}
