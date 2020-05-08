package pl.mkjb.gearbox.external.shared;

import lombok.ToString;

import static pl.mkjb.gearbox.settings.Setting.ZERO_RPM;

@ToString
public class RevGauge implements Event {
    public final int actualRevs;

    public RevGauge(int actualRevs) {
        if (actualRevs < ZERO_RPM) {
            throw new IllegalArgumentException("RPM must be positive");
        }

        this.actualRevs = actualRevs;
    }
}
