package pl.mkjb.gearbox.gearbox.calculators;


import io.vavr.Function1;
import pl.mkjb.gearbox.gearbox.shared.VehicleStatusData;

import static pl.mkjb.gearbox.settings.Setting.FIRST_GEAR;

final class ComfortCalculator implements Calculator {
    @Override
    public Function1<VehicleStatusData, Integer> calculate() {
        return vehicleStatusData -> FIRST_GEAR;
    }
}
