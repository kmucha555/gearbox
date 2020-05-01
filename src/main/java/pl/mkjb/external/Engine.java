package pl.mkjb.external;

import lombok.val;
import pl.mkjb.external.shared.Rev;

import java.util.Random;

import static pl.mkjb.Settings.IDLE_RPM;
import static pl.mkjb.Settings.MAX_RPM;

class Engine {
    private final Random random = new Random();

    public Rev revs() {
        val revs = random.nextInt((MAX_RPM - IDLE_RPM) + IDLE_RPM);
        return new Rev(revs);
    }
}
