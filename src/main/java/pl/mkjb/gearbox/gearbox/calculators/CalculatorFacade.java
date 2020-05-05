package pl.mkjb.gearbox.gearbox.calculators;

import io.vavr.Function1;
import pl.mkjb.gearbox.gearbox.shared.VehicleStatusData;

public class CalculatorFacade {
    private final Calculator ecoCalculator;
    private final Calculator comfortCalculator;
    private final Calculator sportCalculator;

    public CalculatorFacade() {
        this.ecoCalculator = new EcoCalculator();
        this.comfortCalculator = new ComfortCalculator();
        this.sportCalculator = new SportCalculator();
    }

    public Function1<VehicleStatusData, Integer> ecoCalculator() {
        return this.ecoCalculator.calculate();
    }

    public Function1<VehicleStatusData, Integer> comfortCalculator() {
        return this.comfortCalculator.calculate();
    }

    public Function1<VehicleStatusData, Integer> sportCalculator() {
        return this.sportCalculator.calculate();
    }
}
