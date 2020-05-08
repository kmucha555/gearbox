package pl.mkjb.gearbox.gearbox.shared;

import lombok.Builder;
import lombok.ToString;
import pl.mkjb.gearbox.external.shared.BrakeThreshold;
import pl.mkjb.gearbox.external.shared.LinearSpeed;
import pl.mkjb.gearbox.external.shared.RevGauge;
import pl.mkjb.gearbox.external.shared.ThrottleThreshold;
import pl.mkjb.gearbox.settings.AggressiveMode;
import pl.mkjb.gearbox.settings.Mode;
import pl.mkjb.gearbox.settings.State;

@Builder
@ToString
public final class VehicleStatusData {
    public final ThrottleThreshold throttleThreshold;
    public final BrakeThreshold brakeThreshold;
    public final RevGauge revGauge;
    public final LinearSpeed linearSpeed;
    public final State state;
    public final Mode mode;
    public final AggressiveMode aggressiveMode;
    public final Gear currentGear;
}
