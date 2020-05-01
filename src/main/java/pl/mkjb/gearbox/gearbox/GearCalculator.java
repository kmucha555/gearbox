package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import pl.mkjb.gearbox.gearbox.shared.Gear;
import pl.mkjb.gearbox.settings.Mode;

import static pl.mkjb.gearbox.settings.Mode.*;

class GearCalculator {
    private final ExternalSystem externalSystem;
    private final Map<Mode, Function1<DriverInput, Integer>> calculators;

    public GearCalculator(ExternalSystem externalSystem) {
        this.externalSystem = externalSystem;
        this.calculators = HashMap.of(
                ECO, ecoCalc(),
                COMFORT, comfortCalc(),
                SPORT, sportCalc(),
                SPORT_PLUS, sportPlusCalc()
        );
    }

    public Function2<Mode, DriverInput, Gear> calculate() {
        return (mode, driverInput) ->
                calculators.get(mode)
                        .map(calc -> calc.apply(driverInput))
                        .map(Gear::new)
                        .getOrElseThrow(() -> new IllegalArgumentException("Unknown drive mode"));
    }

    private Function1<DriverInput, Integer> ecoCalc() {
        return driverInput -> 0;
    }

    private Function1<DriverInput, Integer> comfortCalc() {
        return driverInput -> 0;
    }

    private Function1<DriverInput, Integer> sportCalc() {
        return driverInput -> 0;
    }

    private Function1<DriverInput, Integer> sportPlusCalc() {
        return driverInput -> 0;
    }
}
