package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Function2;
import lombok.val;
import pl.mkjb.gearbox.gearbox.shared.Gear;

import java.util.function.Predicate;

import static pl.mkjb.gearbox.settings.Setting.*;


final class EcoCalculator implements Calculator {
    private static final int UPSHIFT_REVS = 2000;
    private static final int DOWNSHIFT_REVS = 1000;
    private static final int DOWNSHIFT_WHILE_BRAKING = 1500;

    @Override
    public Function1<VehicleStatusData, Gear> calculate() {
        return vehicleStatusData -> {

            if (isThrottleApplied().test(vehicleStatusData)) {

                if (vehicleStatusData.revGauge.actualRevs <= DOWNSHIFT_REVS) {
                    return newGear().apply(vehicleStatusData, DOWNSHIFT);
                }

                if (vehicleStatusData.revGauge.actualRevs >= UPSHIFT_REVS) {
                    return newGear().apply(vehicleStatusData, UPSHIFT);
                }

                return newGear().apply(vehicleStatusData, NO_GEAR_CHANGE);
            }

            if (isBrakeForceApplied().test(vehicleStatusData)) {

                if (vehicleStatusData.revGauge.actualRevs <= DOWNSHIFT_WHILE_BRAKING) {
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

    private Function2<VehicleStatusData, Integer, Gear> newGear() {
        return (vehicleStatusData, gearChangeScope) -> {
            val newGear = vehicleStatusData.currentGear.gear + gearChangeScope;
            if (newGear < FIRST_GEAR || newGear > MAX_GEAR_NUMBER) {
                return new Gear(vehicleStatusData.currentGear.gear);
            }
            return new Gear(newGear);
        };
    }
}
