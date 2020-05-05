package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Predicates;

import java.util.function.Predicate;

import static pl.mkjb.gearbox.settings.Setting.*;

final class SportCalculator implements Calculator {
    private static final int UPSHIFT_REVS = 5000;
    private static final int DOWNSHIFT_REVS = 1500;
    private static final int DOWNSHIFT_WHILE_BRAKING = 3000;
    private static final int THROTTLE_THRESHOLD_SINGLE_KICKDOWN = 50;
    private static final int THROTTLE_THRESHOLD_DOUBLE_KICKDOWN = 70;
    private static final int KICKDOWN_MAX_REVS_LIMIT = 5000;


    @Override
    public Function1<VehicleStatusData, Integer> calculate() {
        return vehicleStatusData -> {

            if (Predicates.allOf(isThrottleThresholdOverDoubleKickdownLimit(), isBelowDownshiftRevsLimit()).test(vehicleStatusData)) {
                return KICKDOWN;
            }

            if (Predicates.allOf(isThrottleThresholdOverSingleKickdownLimit(), isBelowDownshiftRevsLimit()).test(vehicleStatusData)) {
                return DOWNSHIFT;
            }

            if (Predicates.allOf(isThrottleApplied(), shouldDownShift()).test(vehicleStatusData)) {
                return DOWNSHIFT;
            }

            if (Predicates.allOf(isThrottleApplied(), shouldUpShift()).test(vehicleStatusData)) {
                return UPSHIFT;
            }

            if (isThrottleApplied().and(Predicates.noneOf(shouldUpShift(), shouldDownShift())).test(vehicleStatusData)) {
                return NO_GEAR_CHANGE;
            }

            if (Predicates.allOf(isBrakeForceApplied(), shouldDownShiftWhileBraking()).test(vehicleStatusData)) {
                return DOWNSHIFT;
            }

            if (Predicates.allOf(isBrakeForceApplied().and(Predicates.noneOf(shouldDownShiftWhileBraking()))).test(vehicleStatusData)) {
                return NO_GEAR_CHANGE;
            }

            return FIRST_GEAR;
        };
    }

    private Predicate<VehicleStatusData> isThrottleApplied() {
        return vehicleStatusData -> vehicleStatusData.throttleThreshold.level > ZERO_THRESHOLD;
    }

    private Predicate<VehicleStatusData> isThrottleThresholdOverSingleKickdownLimit() {
        return vehicleStatusData -> vehicleStatusData.throttleThreshold.level >= THROTTLE_THRESHOLD_SINGLE_KICKDOWN;
    }

    private Predicate<VehicleStatusData> isThrottleThresholdOverDoubleKickdownLimit() {
        return vehicleStatusData -> vehicleStatusData.throttleThreshold.level >= THROTTLE_THRESHOLD_DOUBLE_KICKDOWN;
    }

    private Predicate<VehicleStatusData> isBelowDownshiftRevsLimit() {
        return vehicleStatusData -> vehicleStatusData.revGauge.actualRevs <= KICKDOWN_MAX_REVS_LIMIT;
    }

    private Predicate<VehicleStatusData> shouldUpShift() {
        return vehicleStatusData -> vehicleStatusData.revGauge.actualRevs >= UPSHIFT_REVS;
    }

    private Predicate<VehicleStatusData> shouldDownShift() {
        return vehicleStatusData -> vehicleStatusData.revGauge.actualRevs <= DOWNSHIFT_REVS;
    }

    private Predicate<VehicleStatusData> shouldDownShiftWhileBraking() {
        return vehicleStatusData -> vehicleStatusData.revGauge.actualRevs <= DOWNSHIFT_WHILE_BRAKING;
    }

    private Predicate<VehicleStatusData> isBrakeForceApplied() {
        return vehicleStatusData -> vehicleStatusData.brakeThreshold.level > ZERO_THRESHOLD;
    }

}
