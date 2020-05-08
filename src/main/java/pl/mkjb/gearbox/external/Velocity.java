package pl.mkjb.gearbox.external;

import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.external.shared.LinearSpeed;

@RequiredArgsConstructor
final class Velocity implements Component<LinearSpeed> {
    private final EventBus eventBus;

    @Override
    public void sendEvent(LinearSpeed event) {
        eventBus.post(event);
    }
}
