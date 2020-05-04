package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import pl.mkjb.gearbox.gearbox.shared.Gear;
import pl.mkjb.gearbox.settings.Mode;
import pl.mkjb.gearbox.settings.State;

import static io.vavr.API.*;
import static pl.mkjb.gearbox.settings.Mode.*;
import static pl.mkjb.gearbox.settings.Setting.NEUTRAL_GEAR;
import static pl.mkjb.gearbox.settings.Setting.REVERSE_GEAR;
import static pl.mkjb.gearbox.settings.State.*;

class GearCalculator {
    private final Calculator ecoCalculator;
    private final Calculator comfortCalculator;
    private final Calculator sportCalculator;

    public GearCalculator() {
        this.ecoCalculator = new EcoCalculator();
        this.comfortCalculator = new ComfortCalculator();
        this.sportCalculator = new SportCalculator();
    }

    public Function1<VehicleStatusData, Gear> calculate() {
        return vehicleStatusData ->
                selectGearboxState()
                        .apply(vehicleStatusData.state)
                        .apply(vehicleStatusData.mode)
                        .apply(vehicleStatusData);
    }

    private Function1<State, Function1<Mode, Function1<VehicleStatusData, Gear>>> selectGearboxState() {
        return state -> Match(state).of(
                Case($(DRIVE), drive()),
                Case($(PARK), park()),
                Case($(NEUTRAL), neutral()),
                Case($(REVERSE), reverse()));
    }

    private Function1<Mode, Function1<VehicleStatusData, Gear>> drive() {
        return mode -> Match(mode).of(
                Case($(ECO), ecoCalculator.calculate()),
                Case($(COMFORT), comfortCalculator.calculate()),
                Case($(SPORT), sportCalculator.calculate()));
    }

    private Function1<Mode, Function1<VehicleStatusData, Gear>> park() {
        return mode -> vehicleStatusData -> new Gear(NEUTRAL_GEAR);
    }

    private Function1<Mode, Function1<VehicleStatusData, Gear>> neutral() {
        return mode -> vehicleStatusData -> new Gear(NEUTRAL_GEAR);
    }

    private Function1<Mode, Function1<VehicleStatusData, Gear>> reverse() {
        return mode -> vehicleStatusData -> new Gear(REVERSE_GEAR);
    }
}
