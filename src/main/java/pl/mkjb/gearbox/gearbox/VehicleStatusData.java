package pl.mkjb.gearbox.gearbox;

import lombok.Builder;
import lombok.ToString;
import pl.mkjb.gearbox.external.shared.BrakeThreshold;
import pl.mkjb.gearbox.external.shared.LinearSpeed;
import pl.mkjb.gearbox.external.shared.RevGauge;
import pl.mkjb.gearbox.external.shared.ThrottleThreshold;
import pl.mkjb.gearbox.gearbox.shared.Gear;
import pl.mkjb.gearbox.settings.Mode;
import pl.mkjb.gearbox.settings.State;

@Builder
@ToString
class VehicleStatusData {
    public final ThrottleThreshold throttleThreshold;
    public final BrakeThreshold brakeThreshold;
    public final RevGauge revGauge;
    public final LinearSpeed linearSpeed;
    public final State state;
    public final Mode mode;
    public final Gear currentGear;
}