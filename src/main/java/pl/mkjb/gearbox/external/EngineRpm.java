package pl.mkjb.gearbox.external;

import lombok.NoArgsConstructor;
import lombok.val;

import java.util.Random;

@NoArgsConstructor
class EngineRpm {
    private static final int MIN_RPM = 750;
    private static final int MAX_RPM = 7000;
    private final Random random = new Random();

    public int getEngineRpm() {
        val randomRpm = random.nextInt((MAX_RPM - MIN_RPM) + MIN_RPM);
        return randomRpm;
    }
}
