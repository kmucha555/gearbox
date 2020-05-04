package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import pl.mkjb.gearbox.gearbox.shared.Gear;

import static pl.mkjb.gearbox.settings.Setting.FIRST_GEAR;

final class SportCalculator implements Calculator {
    @Override
    public Function1<VehicleStatusData, Gear> calculate() {
        return vehicleStatusData -> new Gear(FIRST_GEAR);
    }
}
