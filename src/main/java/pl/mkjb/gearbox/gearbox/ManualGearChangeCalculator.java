package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Predicates;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.val;
import pl.mkjb.gearbox.gearbox.shared.Gear;
import pl.mkjb.gearbox.gearbox.shared.VehicleStatusData;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static pl.mkjb.gearbox.gearbox.ManualGearChangeCalculator.EngineRevsScopeForManualGearChange.REVS_SCOPE;
import static pl.mkjb.gearbox.settings.Setting.*;

@RequiredArgsConstructor
class ManualGearChangeCalculator {

    @RequiredArgsConstructor
    @Accessors(fluent = true)
    public enum EngineRevsScopeForManualGearChange {
        REVS_SCOPE(1500, 5500);

        private final int minimalUpshiftRevs;
        private final int maximalDownshiftRevs;
    }

    public Function2<Gear, VehicleStatusData, Gear> calculate() {
        return (gearChangeScope, statusData) ->
                newGear().apply(
                        calculateGear().apply(gearChangeScope, statusData)
                );
    }

    private Function1<Integer, Gear> newGear() {
        return Gear::new;
    }

    private Function2<Gear, VehicleStatusData, Integer> calculateGear() {
        return (gearChangeScope, statusData) -> {
            val newGear = statusData.currentGear.gear + gearChangeScope.gear;

            if (isUpshiftPossible().test(gearChangeScope, statusData)) {
                return newGear;
            }

            if (isDownshiftPossible().test(gearChangeScope, statusData)) {
                return newGear;
            }

            return statusData.currentGear.gear;
        };
    }

    private BiPredicate<Gear, VehicleStatusData> isUpshiftPossible() {
        return (gearChangeScope, statusData) -> {
            val newGear = statusData.currentGear.gear + gearChangeScope.gear;
            return Predicates.is(isValidGear().test(newGear))
                    .and(Predicates.is(isUpshifting().test(gearChangeScope)))
                    .and(Predicates.is(isOverUpshiftRevsLimit().test(statusData)))
                    .test(true);
        };
    }

    private BiPredicate<Gear, VehicleStatusData> isDownshiftPossible() {
        return (gearChangeScope, statusData) -> {
            val newGear = statusData.currentGear.gear + gearChangeScope.gear;
            return Predicates.is(isValidGear().test(newGear))
                    .and(Predicates.is(isDownshifting().test(gearChangeScope)))
                    .and(Predicates.is(isOverDownshiftRevsLimit().test(statusData)))
                    .test(true);
        };
    }

    private Predicate<Integer> isValidGear() {
        return givenGear -> givenGear >= FIRST_GEAR && givenGear <= MAX_GEAR_NUMBER;
    }

    private Predicate<Gear> isUpshifting() {
        return givenGear -> givenGear.gear == UPSHIFT;
    }

    private Predicate<Gear> isDownshifting() {
        return givenGear -> givenGear.gear == DOWNSHIFT;
    }

    private Predicate<VehicleStatusData> isOverUpshiftRevsLimit() {
        return statusData -> statusData.revGauge.actualRevs >= REVS_SCOPE.minimalUpshiftRevs;
    }

    private Predicate<VehicleStatusData> isOverDownshiftRevsLimit() {
        return statusData -> statusData.revGauge.actualRevs <= REVS_SCOPE.maximalDownshiftRevs;
    }
}
