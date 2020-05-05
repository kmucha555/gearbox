package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Predicates;

import static pl.mkjb.gearbox.gearbox.CommonCalculator.*;
import static pl.mkjb.gearbox.settings.Setting.*;

final class SportCalculator implements Calculator {

    @Override
    public Function1<VehicleStatusData, Integer> calculate() {
        return statusData -> {

            if (shouldMakeDoubleKickdown(statusData)) {
                return KICKDOWN;
            }

            if (shouldMakeSingleKickdown(statusData)) {
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

    private boolean shouldMakeDoubleKickdown(VehicleStatusData statusData) {
        return Predicates.allOf(isThrottleThresholdOverDoubleKickdownLimit(), isBelowDownshiftRevsLimit()).test(statusData);
    }

    private boolean shouldMakeSingleKickdown(VehicleStatusData statusData) {
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
