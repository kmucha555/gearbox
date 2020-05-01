package pl.mkjb.user;

import lombok.NonNull;
import pl.mkjb.user.shared.GearStickListener;
import pl.mkjb.user.shared.GearboxState;

import java.util.HashSet;
import java.util.Set;

public class GearStick {
    private final Set<GearStickListener> carComponents = new HashSet<>();

    public void register(@NonNull GearStickListener carComponent) {
        carComponents.add(carComponent);
    }

    public void unregister(@NonNull GearStickListener carComponent) {
        carComponents.remove(carComponent);
    }

    public void sendEvent(@NonNull GearboxState event) {
        carComponents.forEach(component -> component.onGearStickPositionChange(event));
    }
}
