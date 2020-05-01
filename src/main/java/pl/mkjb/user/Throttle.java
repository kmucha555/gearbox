package pl.mkjb.user;

import lombok.NonNull;
import pl.mkjb.user.shared.ThrottleListener;
import pl.mkjb.user.shared.ThrottleThreshold;

import java.util.HashSet;
import java.util.Set;

public class Throttle {
    private final Set<ThrottleListener> carComponents = new HashSet<>();

    public void register(@NonNull ThrottleListener carComponent) {
        carComponents.add(carComponent);
    }

    public void unregister(@NonNull ThrottleListener carComponent) {
        carComponents.remove(carComponent);
    }

    public void sendEvent(@NonNull ThrottleThreshold event) {
        carComponents.forEach(component -> component.onThrottleChange(event));
    }
}
