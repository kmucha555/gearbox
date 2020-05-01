package pl.mkjb.gearbox.external;

import com.google.common.eventbus.EventBus;
import lombok.AccessLevel;
import lombok.Builder;
import pl.mkjb.gearbox.external.shared.BrakeThreshold;
import pl.mkjb.gearbox.external.shared.LinearSpeed;
import pl.mkjb.gearbox.external.shared.RevGauge;
import pl.mkjb.gearbox.external.shared.ThrottleThreshold;
import pl.mkjb.gearbox.settings.GearboxState;

import static pl.mkjb.gearbox.settings.GearboxState.PARK;
import static pl.mkjb.gearbox.settings.Setting.*;

@Builder(access = AccessLevel.PRIVATE)
public class ExternalFacade {
    private final EventBus eventBus;
    private final Component<ThrottleThreshold> throttle;
    private final Component<BrakeThreshold> brake;
    private final Component<RevGauge> engine;
    private final Component<GearboxState> gearStick;
    private final Component<LinearSpeed> velocity;

    public static ExternalFacade connectExternalSystem(EventBus eventBus) {
        return ExternalFacade.builder()
                .eventBus(eventBus)
                .throttle(new Throttle(eventBus))
                .brake(new Brake(eventBus))
                .engine(new Engine(eventBus))
                .gearStick(new GearStick(eventBus))
                .velocity(new Velocity(eventBus))
                .build();
    }

    public void stop() {
        throttle.sendEvent(new ThrottleThreshold(MIN_THRESHOLD));
        brake.sendEvent(new BrakeThreshold(MIN_THRESHOLD));
        engine.sendEvent(new RevGauge(IDLE_RPM));
        velocity.sendEvent(new LinearSpeed(NO_SPEED));
        gearStick.sendEvent(PARK);
    }
}
