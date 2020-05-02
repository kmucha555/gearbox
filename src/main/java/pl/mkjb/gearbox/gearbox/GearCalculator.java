package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import pl.mkjb.gearbox.gearbox.shared.Gear;
import pl.mkjb.gearbox.settings.Mode;

import static pl.mkjb.gearbox.settings.Mode.*;

class GearCalculator {
    private final Map<Mode, Function1<VehicleStatusData, Integer>> calculators;

    public GearCalculator() {
        this.calculators = HashMap.of(
                ECO, ecoCalc(),
                COMFORT, comfortCalc(),
                SPORT, sportCalc(),
                SPORT_PLUS, sportPlusCalc()
        );
    }

    public Function1<VehicleStatusData, Gear> calculate() {
        return vehicleStatusData ->
                calculators.get(vehicleStatusData.mode)
                        .map(calc -> calc.apply(vehicleStatusData))
                        .map(Gear::new)
                        .getOrElseThrow(() -> new IllegalArgumentException("Unknown drive mode"));
    }

    private Function1<VehicleStatusData, Integer> ecoCalc() {
        return vehicleStatusData -> 0;
    }

    private Function1<VehicleStatusData, Integer> comfortCalc() {
        return vehicleStatusData -> 0;
    }

    private Function1<VehicleStatusData, Integer> sportCalc() {
        return vehicleStatusData -> 0;
    }

    private Function1<VehicleStatusData, Integer> sportPlusCalc() {
        return vehicleStatusData -> 0;
    }
}
