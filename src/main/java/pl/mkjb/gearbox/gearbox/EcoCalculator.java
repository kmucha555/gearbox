package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Predicates;

import java.util.function.Predicate;

import static pl.mkjb.gearbox.settings.Setting.*;


final class EcoCalculator implements Calculator {
    private static final int UPSHIFT_REVS = 2000;
    private static final int DOWNSHIFT_REVS = 1000;
    private static final int DOWNSHIFT_WHILE_BRAKING = 1500;

    @Override
    public Function1<VehicleStatusData, Integer> calculate() {
        return vehicleStatusData -> {

            if (isThrottleAppliedAndRevsAreOverUpshiftLimit(vehicleStatusData)) {
                return UPSHIFT;
            }

            if (isThrottleAppliedAndRevsAreBelowDownshiftLimit(vehicleStatusData)) {
                return DOWNSHIFT;
            }

            if (isBrakeAppliedAndRevsAreBelowDownShiftWhileBrakingLimit(vehicleStatusData)) {
                return DOWNSHIFT;
            }

            if (isThrottleAppliedAndRevsAreBetweenUpAndDownShiftLimits(vehicleStatusData)) {
                return NO_GEAR_CHANGE;
            }

            if (isBrakeAppliedAndRevsAreOverDownshiftWhileBrakingLimit(vehicleStatusData)) {
                return NO_GEAR_CHANGE;
            }

            return FIRST_GEAR;
        };
    }

    private boolean isThrottleAppliedAndRevsAreOverUpshiftLimit(VehicleStatusData vehicleStatusData) {
        return Predicates.allOf(isThrottleApplied(), shouldUpShift()).test(vehicleStatusData);
    }

    private boolean isThrottleAppliedAndRevsAreBelowDownshiftLimit(VehicleStatusData vehicleStatusData) {
        return Predicates.allOf(isThrottleApplied(), shouldDownShift()).test(vehicleStatusData);
    }

    private boolean isBrakeAppliedAndRevsAreBelowDownShiftWhileBrakingLimit(VehicleStatusData vehicleStatusData) {
        return Predicates.allOf(isBrakeForceApplied(), shouldDownShiftWhileBraking()).test(vehicleStatusData);
    }

    private boolean isThrottleAppliedAndRevsAreBetweenUpAndDownShiftLimits(VehicleStatusData vehicleStatusData) {
        return isThrottleApplied().and(Predicates.noneOf(shouldDownShift(), shouldUpShift())).test(vehicleStatusData);
    }

    private boolean isBrakeAppliedAndRevsAreOverDownshiftWhileBrakingLimit(VehicleStatusData vehicleStatusData) {
        return isBrakeForceApplied().and(Predicates.noneOf(shouldDownShiftWhileBraking())).test(vehicleStatusData);
    }

    private Predicate<VehicleStatusData> isThrottleApplied() {
        return vehicleStatusData -> vehicleStatusData.throttleThreshold.level > ZERO_THRESHOLD;
    }

    private Predicate<VehicleStatusData> isBrakeForceApplied() {
        return vehicleStatusData -> vehicleStatusData.brakeThreshold.level > ZERO_THRESHOLD;
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
}
