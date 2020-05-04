package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;

public interface Calculator {
    Function1<VehicleStatusData, Integer> calculate();
}
