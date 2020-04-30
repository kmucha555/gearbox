package pl.mkjb.gearbox.external;

import lombok.NonNull;
import pl.mkjb.gearbox.external.shared.Listener;
import pl.mkjb.gearbox.external.shared.ThrottleThreshold;

import java.util.HashSet;
import java.util.Set;

public class Throttle {
    private final Set<Listener> carComponents = new HashSet<>();

    public void register(@NonNull Listener carComponent) {
        carComponents.add(carComponent);
    }

    public void unregister(@NonNull Listener carComponent) {
        carComponents.remove(carComponent);
    }

    public void sendEvent(@NonNull ThrottleThreshold event) {
        carComponents.forEach(component -> component.onThrottleChange(event));
    }
}
