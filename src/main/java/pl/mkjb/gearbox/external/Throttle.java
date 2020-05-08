package pl.mkjb.gearbox.external;

import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.external.shared.ThrottleThreshold;

@RequiredArgsConstructor
final class Throttle implements Component<ThrottleThreshold> {
    private final EventBus eventBus;

    @Override
    public void sendEvent(ThrottleThreshold event) {
        eventBus.post(event);
    }
}
