package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Function2;
import lombok.val;

import java.util.function.Predicate;

import static pl.mkjb.gearbox.settings.DriveModeSetup.EcoSetup;
import static pl.mkjb.gearbox.settings.Setting.*;


final class EcoCalculator implements Calculator {
    @Override
    public Function1<VehicleStatusData, Integer> calculate() {
        return vehicleStatusData -> {

            if (isThrottleApplied().test(vehicleStatusData)) {

                if (vehicleStatusData.revGauge.actualRevs <= EcoSetup.DOWNSHIFT_REVS) {
                    return newGear().apply(vehicleStatusData, DOWNSHIFT);
                }

                if (vehicleStatusData.revGauge.actualRevs >= EcoSetup.UPSHIFT_REVS) {
                    return newGear().apply(vehicleStatusData, UPSHIFT);
                }

                return newGear().apply(vehicleStatusData, NO_GEAR_CHANGE);
            }

            if (isBrakeForceApplied().test(vehicleStatusData)) {

                if (vehicleStatusData.revGauge.actualRevs <= EcoSetup.DOWNSHIFT_WHILE_BRAKING) {
                    return newGear().apply(vehicleStatusData, DOWNSHIFT);
                }

                return newGear().apply(vehicleStatusData, NO_GEAR_CHANGE);
            }

            return newGear().apply(vehicleStatusData, FIRST_GEAR);
        };
    }

    private Predicate<VehicleStatusData> isThrottleApplied() {
        return vehicleStatusData -> vehicleStatusData.throttleThreshold.level > ZERO_THRESHOLD;
    }

    private Predicate<VehicleStatusData> isBrakeForceApplied() {
        return vehicleStatusData -> vehicleStatusData.brakeThreshold.level > ZERO_THRESHOLD;
    }

    private Function2<VehicleStatusData, Integer, Integer> newGear() {
        return (vehicleStatusData, gearChangeScope) -> {
            val newGear = vehicleStatusData.currentGear.gear + gearChangeScope;
            if (newGear < FIRST_GEAR || newGear > MAX_GEAR_NUMBER) {
                return vehicleStatusData.currentGear.gear;
            }
            return newGear;
        };
    }
}
