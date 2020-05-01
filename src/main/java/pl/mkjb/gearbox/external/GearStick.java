package pl.mkjb.gearbox.external;

import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.settings.GearboxState;

@RequiredArgsConstructor
class GearStick implements Component<GearboxState> {
    private final EventBus eventBus;

    @Override
    public void sendEvent(GearboxState event) {
        eventBus.post(event);
    }
}
