package pl.mkjb.gearbox.gearbox;

import io.vavr.Function1;
import io.vavr.Function2;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import pl.mkjb.gearbox.settings.Mode;

import java.util.function.Predicate;

import static io.vavr.API.*;
import static pl.mkjb.gearbox.gearbox.CommonCalculator.DriveModeSettings.*;
import static pl.mkjb.gearbox.settings.AggressiveMode.*;
import static pl.mkjb.gearbox.settings.Mode.ECO;
import static pl.mkjb.gearbox.settings.Setting.ZERO_THRESHOLD;

final class CommonCalculator {

    @RequiredArgsConstructor
    @Accessors(fluent = true)
    public enum DriveModeSettings {
        ECO_SETTINGS(2000, 1000, 1500, -1, 0, 0),
        COMFORT_SETTINGS(2500, 1000, 2000, 4500, 50, -1),
        SPORT_SETTINGS(5000, 1500, 3000, 5000, 50, 70);

        private final int upshiftRevs;
        private final int downshiftRevs;
        private final int downshiftRevsWhileBraking;
        private final int kickdownMaxRevsLimit;
        private final int throttleThresholdSingleKickdown;
        private final int throttleThresholdDoubleKickdown;
    }

    public static Predicate<VehicleStatusData> isThrottleThresholdOverSingleKickdownLimit() {
        return statusData -> statusData.throttleThreshold.level >= driveModeSettings().apply(statusData).throttleThresholdSingleKickdown;
    }

    public static Predicate<VehicleStatusData> isThrottleThresholdOverDoubleKickdownLimit() {
        return statusData -> statusData.throttleThreshold.level >= driveModeSettings().apply(statusData).throttleThresholdDoubleKickdown;
    }

    public static Predicate<VehicleStatusData> isBelowDownshiftRevsLimit() {
        return statusData -> statusData.revGauge.actualRevs <= newRevsLimit().apply(statusData, driveModeSettings().apply(statusData).kickdownMaxRevsLimit);
    }

    public static Predicate<VehicleStatusData> shouldUpShift() {
        return statusData -> statusData.revGauge.actualRevs >= newRevsLimit().apply(statusData, driveModeSettings().apply(statusData).upshiftRevs);
    }

    public static Predicate<VehicleStatusData> shouldDownShift() {
        return statusData -> statusData.revGauge.actualRevs <= newRevsLimit().apply(statusData, driveModeSettings().apply(statusData).downshiftRevs);
    }

    public static Predicate<VehicleStatusData> shouldDownShiftWhileBraking() {
        return statusData -> statusData.revGauge.actualRevs <= newRevsLimit().apply(statusData, driveModeSettings().apply(statusData).downshiftRevsWhileBraking);
    }

    public static Predicate<VehicleStatusData> isThrottleApplied() {
        return statusData -> statusData.throttleThreshold.level > ZERO_THRESHOLD;
    }

    public static Predicate<VehicleStatusData> isBrakeForceApplied() {
        return statusData -> statusData.brakeThreshold.level > ZERO_THRESHOLD;
    }

    private static Function1<VehicleStatusData, DriveModeSettings> driveModeSettings() {
        return statusData ->
                Match(statusData.mode).of(
                        Case($(ECO), ECO_SETTINGS),
                        Case($(Mode.COMFORT), COMFORT_SETTINGS),
                        Case($(Mode.SPORT), SPORT_SETTINGS));
    }

    private static Function1<VehicleStatusData, Function1<Integer, Integer>> aggressiveMode() {
        return statusData ->
                Match(statusData.aggressiveMode).of(
                        Case($(SOFT), newRevsCalculator().apply(SOFT.raiseRevsFactor())),
                        Case($(HARD), newRevsCalculator().apply(HARD.raiseRevsFactor())),
                        Case($(EXTREME), newRevsCalculator().apply(EXTREME.raiseRevsFactor())));
    }

    private static Function2<VehicleStatusData, Integer, Integer> newRevsLimit() {
        return (statusData, currentRevsLimit) -> aggressiveMode().apply(statusData).apply(currentRevsLimit);
    }

    private static Function2<Integer, Integer, Integer> newRevsCalculator() {
        return (originalRevs, factor) -> originalRevs * (factor / 100);
    }
}
