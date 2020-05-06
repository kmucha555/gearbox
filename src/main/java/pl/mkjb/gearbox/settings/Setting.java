package pl.mkjb.gearbox.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Setting {
    public static final int UPSHIFT = 1;
    public static final int DOWNSHIFT = -1;
    public static final int KICKDOWN = -2;
    public static final int NO_GEAR_CHANGE = 0;
    public static final int NEUTRAL_GEAR = 0;
    public static final int FIRST_GEAR = 1;
    public static final int SECOND_GEAR = 2;
    public static final int THIRD_GEAR = 3;
    public static final int FOURTH_GEAR = 4;
    public static final int REVERSE_GEAR = -1;

    public static final int ZERO_THRESHOLD = 0;
    public static final int SOME_THRESHOLD = 30;
    public static final int MORE_THRESHOLD = 60;
    public static final int MAX_THRESHOLD = 100;

    public static final int MIN_GEAR_NUMBER = -1;
    public static final int MAX_GEAR_NUMBER = 8;

    public static final int ZERO_RPM = 0;
    public static final int IDLE_RPM = 750;
    public static final int VERY_LOW_RPM = 1000;
    public static final int LOW_RPM = 1500;
    public static final int MEDIUM_RPM = 3000;
    public static final int HIGH_RPM = 5000;
    public static final int VERY_HIGH_RPM = 6500;

    public static final int MIN_LINEAR_SPEED = -50;
    public static final int NO_SPEED = 0;
    public static final int SOME_SPEED = 50;
    public static final int MAX_LINEAR_SPEED = 250;
}
