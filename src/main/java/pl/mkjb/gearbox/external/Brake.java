package pl.mkjb.gearbox.external;

import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.external.shared.BrakeThreshold;

@RequiredArgsConstructor
final class Brake implements Component<BrakeThreshold> {
    private final EventBus eventBus;

    @Override
    public void sendEvent(BrakeThreshold event) {
        eventBus.post(event);
    }
}
