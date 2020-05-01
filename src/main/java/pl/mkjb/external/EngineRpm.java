package pl.mkjb.external;

import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor
class EngineRpm {
    private static final int MIN_RPM = 750;
    private static final int MAX_RPM = 7000;
    private final Random random = new Random();

    public int getEngineRpm() {
        return random.nextInt((MAX_RPM - MIN_RPM) + MIN_RPM);
    }
}
