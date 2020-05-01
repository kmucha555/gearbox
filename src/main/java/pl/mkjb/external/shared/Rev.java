package pl.mkjb.external.shared;

import static pl.mkjb.Settings.ZERO_RPM;

public class Rev {
    public final int actualRevs;

    public Rev(int actualRevs) {
        if (actualRevs < ZERO_RPM) {
            throw new IllegalArgumentException("RPM must be positive");
        }

        this.actualRevs = actualRevs;
    }
}
