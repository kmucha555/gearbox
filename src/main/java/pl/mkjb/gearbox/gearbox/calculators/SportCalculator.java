package pl.mkjb.gearbox.gearbox.calculators;

import io.vavr.Function1;
import io.vavr.Predicates;
import pl.mkjb.gearbox.gearbox.shared.VehicleStatusData;

import static pl.mkjb.gearbox.gearbox.calculators.CommonCalculator.*;
import static pl.mkjb.gearbox.settings.Setting.*;

final class SportCalculator implements Calculator {

    @Override
    public Function1<VehicleStatusData, Integer> calculate() {
        return statusData -> {

            if (isPossibleToMakeDoubleKickdown(statusData)) {
                return KICKDOWN;
            }

            if (isPossibleToMakeSingleKickdown(statusData)) {
                return DOWNSHIFT;
            }

            if (shouldDownshift(statusData)) {
                return DOWNSHIFT;
            }

            if (shouldUpshift(statusData)) {
                return UPSHIFT;
            }

            if (shouldNotChangeGearWhileAccelerating(statusData)) {
                return NO_GEAR_CHANGE;
            }

            if (shouldDownshiftWhileBraking(statusData)) {
                return DOWNSHIFT;
            }

            if (shouldNotChangeGearWhileBraking(statusData)) {
                return NO_GEAR_CHANGE;
            }

            return FIRST_GEAR;
        };
    }

    private boolean isPossibleToMakeDoubleKickdown(VehicleStatusData statusData) {
        return Predicates.allOf(isThrottleThresholdOverDoubleKickdownLimit(), isBelowDownshiftRevsLimit()).test(statusData);
    }

    private boolean isPossibleToMakeSingleKickdown(VehicleStatusData statusData) {
        return Predicates.allOf(isThrottleThresholdOverSingleKickdownLimit(), isBelowDownshiftRevsLimit()).test(statusData);
    }

    private boolean shouldDownshift(VehicleStatusData statusData) {
        return Predicates.allOf(isThrottleApplied(), shouldDownShift()).test(statusData);
    }

    private boolean shouldUpshift(VehicleStatusData statusData) {
        return Predicates.allOf(isThrottleApplied(), shouldUpShift()).test(statusData);
    }

    private boolean shouldNotChangeGearWhileAccelerating(VehicleStatusData statusData) {
        return isThrottleApplied().and(Predicates.noneOf(shouldUpShift(), shouldDownShift())).test(statusData);
    }

    private boolean shouldDownshiftWhileBraking(VehicleStatusData statusData) {
        return Predicates.allOf(isBrakeForceApplied(), shouldDownShiftWhileBraking()).test(statusData);
    }

    private boolean shouldNotChangeGearWhileBraking(VehicleStatusData statusData) {
        return Predicates.allOf(isBrakeForceApplied().and(Predicates.noneOf(shouldDownShiftWhileBraking()))).test(statusData);
    }
}
