package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import pl.mkjb.gearbox.settings.State;

import static pl.mkjb.gearbox.settings.State.*;

class GearboxState {
    private final ExternalSystem externalSystem;
    private final Map<State, Function1<DriverInput, State>> states;

    public GearboxState(ExternalSystem externalSystem,
                        Map<State, Function1<DriverInput, Integer>> states) {

        this.externalSystem = externalSystem;
        this.states = HashMap.of(
                PARK, park(),
                REVERSE, reverse(),
                NEUTRAL, neutral(),
                DRIVE, drive()
        );
    }

    public Function2<State, DriverInput, State> change() {
        return (state, driverInput) ->
                states.get(state)
                        .map(calc -> calc.apply(driverInput))
                        .getOrElseThrow(() -> new IllegalArgumentException("Unknown drive mode"));
    }

    private Function1<DriverInput, State> park() {
        return driverInput -> PARK;
    }

    private Function1<DriverInput, State> neutral() {
        return driverInput -> NEUTRAL;
    }

    private Function1<DriverInput, State> drive() {
        return driverInput -> DRIVE;
    }

    private Function1<DriverInput, State> reverse() {
        return driverInput -> REVERSE;
    }
}
