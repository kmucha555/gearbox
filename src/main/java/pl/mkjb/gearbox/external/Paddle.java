package pl.mkjb.gearbox.external;

import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.gearbox.shared.Gear;

@RequiredArgsConstructor
final class Paddle implements Component<Gear> {
    private final EventBus eventBus;

    @Override
    public void sendEvent(Gear event) {
        eventBus.post(event);
    }
}
