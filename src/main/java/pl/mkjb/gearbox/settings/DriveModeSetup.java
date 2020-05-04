package pl.mkjb.gearbox.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DriveModeSetup {
//    public static final EcoSetup ECO_SETUP = EcoSetup;

    public static class EcoSetup {
        public final static int throttleThreshold = 1;
        public final static int upshiftRevs = 2000;
        public final static int downshiftRevs = 1000;
        public final static int downshiftWhileBraking = 1500;
    }
}
