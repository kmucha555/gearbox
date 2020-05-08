package pl.mkjb.gearbox.settings;

import lombok.RequiredArgsConstructor;
import pl.mkjb.gearbox.external.shared.Event;

@RequiredArgsConstructor
public enum AggressiveMode implements Event {
    SOFT(100),
    HARD(130),
    EXTREME(130);

    private final int raiseRevsFactor;

    public int raiseRevsFactor() {
        return raiseRevsFactor;
    }
}
