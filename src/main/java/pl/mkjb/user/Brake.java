package pl.mkjb.user;

import lombok.NonNull;
import pl.mkjb.user.shared.BrakeListener;
import pl.mkjb.user.shared.BrakeThreshold;

import java.util.HashSet;
import java.util.Set;

public class Brake {
    private final Set<BrakeListener> carComponents = new HashSet<>();

    public void register(@NonNull BrakeListener carComponent) {
        carComponents.add(carComponent);
    }

    public void unregister(@NonNull BrakeListener carComponent) {
        carComponents.remove(carComponent);
    }

    public void sendEvent(@NonNull BrakeThreshold event) {
        carComponents.forEach(component -> component.onBrakeApplied(event));
    }
}
