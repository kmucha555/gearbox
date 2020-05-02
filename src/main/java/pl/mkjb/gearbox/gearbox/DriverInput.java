package pl.mkjb.gearbox.gearbox;

import lombok.Builder;
import pl.mkjb.gearbox.external.shared.BrakeThreshold;
import pl.mkjb.gearbox.external.shared.ThrottleThreshold;
import pl.mkjb.gearbox.settings.Mode;
import pl.mkjb.gearbox.settings.State;

@Builder
class DriverInput {
    public final ThrottleThreshold throttleThreshold;
    public final BrakeThreshold brakeThreshold;
    public final State state;
    public final Mode mode;
}
