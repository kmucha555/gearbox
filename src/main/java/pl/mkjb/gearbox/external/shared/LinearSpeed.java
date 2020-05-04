package pl.mkjb.gearbox.external.shared;

import lombok.ToString;

import static pl.mkjb.gearbox.settings.Setting.MAX_LINEAR_SPEED;
import static pl.mkjb.gearbox.settings.Setting.MIN_LINEAR_SPEED;

@ToString
public class LinearSpeed implements Event {
    public final int actualSpeed;

    public LinearSpeed(int actualSpeed) {
        if (actualSpeed < MIN_LINEAR_SPEED || actualSpeed > MAX_LINEAR_SPEED) {
            throw new IllegalArgumentException("Linear velocity out of bounds");
        }

        this.actualSpeed = actualSpeed;
    }
}
