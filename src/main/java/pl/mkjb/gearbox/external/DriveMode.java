package pl.mkjb.gearbox.external;

import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.settings.Mode;

@RequiredArgsConstructor
final class DriveMode implements Component<Mode> {
    private final EventBus eventBus;

    @Override
    public void sendEvent(Mode event) {
        eventBus.post(event);
    }
}
