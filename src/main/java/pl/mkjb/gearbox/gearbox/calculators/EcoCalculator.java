package pl.mkjb.gearbox.gearbox.calculators;

import io.vavr.Function1;
import io.vavr.Predicates;
import pl.mkjb.gearbox.gearbox.shared.VehicleStatusData;

import static pl.mkjb.gearbox.gearbox.calculators.CommonCalculator.*;
import static pl.mkjb.gearbox.settings.Setting.*;


final class EcoCalculator implements Calculator {

    @Override
    public Function1<VehicleStatusData, Integer> calculate() {
        return statusData -> {

            if (shouldUpshiftWhileAccelerating(statusData)) {
                return UPSHIFT;
            }

            if (shouldDownshifthWhileAccelerating(statusData)) {
                return DOWNSHIFT;
            }

            if (shouldDownshiftWhileBraking(statusData)) {
                return DOWNSHIFT;
            }

            if (shouldNotChangeGearWhileAccelerating(statusData)) {
                return NO_GEAR_CHANGE;
            }

            if (shoudNotChangeGearWhileBraking(statusData)) {
                return NO_GEAR_CHANGE;
            }

            return FIRST_GEAR;
        };
    }

    private boolean shouldUpshiftWhileAccelerating(VehicleStatusData statusData) {
        return Predicates.allOf(isThrottleApplied(), shouldUpShift()).test(statusData);
    }

    private boolean shouldDownshifthWhileAccelerating(VehicleStatusData statusData) {
        return Predicates.allOf(isThrottleApplied(), shouldDownShift()).test(statusData);
    }

    private boolean shouldNotChangeGearWhileAccelerating(VehicleStatusData statusData) {
        return isThrottleApplied().and(Predicates.noneOf(shouldDownShift(), shouldUpShift())).test(statusData);
    }

    private boolean shouldDownshiftWhileBraking(VehicleStatusData statusData) {
        return Predicates.allOf(isBrakeForceApplied(), shouldDownShiftWhileBraking()).test(statusData);
    }

    private boolean shoudNotChangeGearWhileBraking(VehicleStatusData statusData) {
        return isBrakeForceApplied().and(Predicates.noneOf(shouldDownShiftWhileBraking())).test(statusData);
    }
}
