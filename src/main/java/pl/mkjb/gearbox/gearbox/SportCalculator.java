package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;

import static pl.mkjb.gearbox.settings.Setting.FIRST_GEAR;

final class SportCalculator implements Calculator {
    @Override
    public Function1<VehicleStatusData, Integer> calculate() {
        return vehicleStatusData -> FIRST_GEAR;
    }
}
