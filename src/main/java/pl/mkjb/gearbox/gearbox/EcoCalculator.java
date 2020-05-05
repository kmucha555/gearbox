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

            if (shouldUpshiftWhileAccelerating(vehicleStatusData)) {
                return UPSHIFT;
            }

            if (shouldDownshifthWhileAccelerating(vehicleStatusData)) {
                return DOWNSHIFT;
            }

            if (shouldDownshiftWhileBraking(vehicleStatusData)) {
                return DOWNSHIFT;
            }

            if (shouldNotChangeGearWhileAccelerating(vehicleStatusData)) {
                return NO_GEAR_CHANGE;
            }

            if (shoudNotChangeGearWhileBraking(vehicleStatusData)) {
                return NO_GEAR_CHANGE;
            }

            return FIRST_GEAR;
        };
    }

    private boolean shouldUpshiftWhileAccelerating(VehicleStatusData vehicleStatusData) {
        return Predicates.allOf(isThrottleApplied(), shouldUpShift()).test(vehicleStatusData);
    }

    private boolean shouldDownshifthWhileAccelerating(VehicleStatusData vehicleStatusData) {
        return Predicates.allOf(isThrottleApplied(), shouldDownShift()).test(vehicleStatusData);
    }

    private boolean shouldNotChangeGearWhileAccelerating(VehicleStatusData vehicleStatusData) {
        return isThrottleApplied().and(Predicates.noneOf(shouldDownShift(), shouldUpShift())).test(vehicleStatusData);
    }

    private boolean shouldDownshiftWhileBraking(VehicleStatusData vehicleStatusData) {
        return Predicates.allOf(isBrakeForceApplied(), shouldDownShiftWhileBraking()).test(vehicleStatusData);
    }

    private boolean shoudNotChangeGearWhileBraking(VehicleStatusData vehicleStatusData) {
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
