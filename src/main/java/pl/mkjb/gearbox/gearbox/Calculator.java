package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import pl.mkjb.gearbox.gearbox.shared.Gear;

public interface Calculator {
    Function1<VehicleStatusData, Gear> calculate();
}
