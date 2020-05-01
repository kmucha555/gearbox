package pl.mkjb.gearbox.external;

import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.external.shared.RevGauge;

@RequiredArgsConstructor
class Engine implements Component<RevGauge> {
    private final EventBus eventBus;

    @Override
    public void sendEvent(RevGauge event) {
        eventBus.post(event);
    }
}
