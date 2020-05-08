package pl.mkjb.gearbox.external;

import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.settings.AggressiveMode;

@RequiredArgsConstructor
final class GearChangeMode implements Component<AggressiveMode> {
    private final EventBus eventBus;

    @Override
    public void sendEvent(AggressiveMode event) {
        eventBus.post(event);
    }
}
