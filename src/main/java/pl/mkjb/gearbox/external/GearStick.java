package pl.mkjb.gearbox.external;

import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.settings.State;

@RequiredArgsConstructor
final class GearStick implements Component<State> {
    private final EventBus eventBus;

    @Override
    public void sendEvent(State event) {
        eventBus.post(event);
    }
}
