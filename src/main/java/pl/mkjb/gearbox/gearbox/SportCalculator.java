package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;

public class SportCalculator implements Calculator {
    @Override
    public Function1<VehicleStatusData, Integer> calculate() {
        return vehicleStatusData -> 1;
    }
}
