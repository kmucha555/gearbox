package pl.mkjb.gearbox.gearbox.calculators;

import io.vavr.Function1;
import pl.mkjb.gearbox.gearbox.shared.VehicleStatusData;

public interface Calculator {
    Function1<VehicleStatusData, Integer> calculate();
}
