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
    private final Calculator sportPlusCalculator;

    public GearCalculator() {
        this.ecoCalculator = new EcoCalculator();
        this.comfortCalculator = new ComfortCalculator();
        this.sportCalculator = new SportCalculator();
        this.sportPlusCalculator = new SportPlusEcoCalculator();
    }

    public Function1<VehicleStatusData, Gear> calculate() {
        return vehicleStatusData ->
                new Gear(
                        selectGearboxState()
                                .apply(vehicleStatusData.state)
                                .apply(vehicleStatusData.mode)
                                .apply(vehicleStatusData)
                );
    }

    private Function1<State, Function1<Mode, Function1<VehicleStatusData, Integer>>> selectGearboxState() {
        return state -> Match(state).of(
                Case($(DRIVE), drive()),
                Case($(PARK), park()),
                Case($(NEUTRAL), neutral()),
                Case($(REVERSE), reverse()));
    }

    private Function1<Mode, Function1<VehicleStatusData, Integer>> drive() {
        return mode -> Match(mode).of(
                Case($(ECO), ecoCalculator.calculate()),
                Case($(COMFORT), comfortCalculator.calculate()),
                Case($(SPORT), sportCalculator.calculate()),
                Case($(SPORT_PLUS), sportPlusCalculator.calculate()));
    }

    private Function1<Mode, Function1<VehicleStatusData, Integer>> park() {
        return mode -> vehicleStatusData -> NEUTRAL_GEAR;
    }

    private Function1<Mode, Function1<VehicleStatusData, Integer>> neutral() {
        return mode -> vehicleStatusData -> NEUTRAL_GEAR;
    }

    private Function1<Mode, Function1<VehicleStatusData, Integer>> reverse() {
        return mode -> vehicleStatusData -> REVERSE_GEAR;
    }
}
