package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;

public class ComfortCalculator implements Calculator {
    @Override
    public Function1<VehicleStatusData, Integer> calculate() {
        return vehicleStatusData -> 1;
    }
}
