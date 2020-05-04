package pl.mkjb.gearbox.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DriveModeSetup {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class EcoSetup {
        public static final int UPSHIFT_REVS = 2000;
        public static final int DOWNSHIFT_REVS = 1000;
        public static final int DOWNSHIFT_WHILE_BRAKING = 1500;
    }
}
