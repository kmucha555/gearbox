package pl.mkjb.gearbox

import groovy.transform.CompileStatic
import pl.mkjb.gearbox.external.shared.BrakeThreshold
import pl.mkjb.gearbox.external.shared.LinearSpeed
import pl.mkjb.gearbox.external.shared.RevGauge
import pl.mkjb.gearbox.external.shared.ThrottleThreshold
import pl.mkjb.gearbox.gearbox.GearboxDriver
import pl.mkjb.gearbox.gearbox.shared.Gear
import pl.mkjb.gearbox.settings.AggressiveMode

import static pl.mkjb.gearbox.settings.AggressiveMode.HARD
import static pl.mkjb.gearbox.settings.AggressiveMode.SOFT
import static pl.mkjb.gearbox.settings.Mode.COMFORT
import static pl.mkjb.gearbox.settings.Mode.ECO
import static pl.mkjb.gearbox.settings.Mode.SPORT
import static pl.mkjb.gearbox.settings.Setting.*
import static pl.mkjb.gearbox.settings.State.DRIVE
import static pl.mkjb.gearbox.settings.State.MANUAL

@CompileStatic
trait PreparedInput {

    Gear reverseGear = new Gear(REVERSE_GEAR)
    Gear neutralGear = new Gear(NEUTRAL_GEAR)
    Gear firstGear = new Gear(FIRST_GEAR)
    Gear secondGear = new Gear(SECOND_GEAR)
    Gear thirdGear = new Gear(THIRD_GEAR)
    Gear fourthGear = new Gear(FOURTH_GEAR)
    Gear maxGear = new Gear(MAX_GEAR_NUMBER)
    Gear upshift = new Gear(UPSHIFT)
    Gear downshift = new Gear(DOWNSHIFT)

    LinearSpeed zeroLinearSpeed = new LinearSpeed(NO_SPEED)
    LinearSpeed someLinearSpeed = new LinearSpeed(SOME_SPEED)

    BrakeThreshold halfBrakeThreshold = new BrakeThreshold(SOME_THRESHOLD)
    BrakeThreshold zeroBrakeThreshold = new BrakeThreshold(ZERO_THRESHOLD)

    ThrottleThreshold belowKickDownThrottle = new ThrottleThreshold(SOME_THRESHOLD)
    ThrottleThreshold singleKickDownThrottle = new ThrottleThreshold(MORE_THRESHOLD)
    ThrottleThreshold doubleKickDownThrottle = new ThrottleThreshold(MAX_THRESHOLD)

    RevGauge veryLowRpm = new RevGauge(VERY_LOW_RPM)
    RevGauge lowRpm = new RevGauge(LOW_RPM)
    RevGauge mediumRpm = new RevGauge(MEDIUM_RPM)
    RevGauge highRpm = new RevGauge(HIGH_RPM)
    RevGauge veryHighRpm = new RevGauge(VERY_HIGH_RPM)

    AggressiveMode softChange = SOFT
    AggressiveMode hardChange = HARD

    def changeToDrive(GearboxDriver gearboxDriver) {
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(DRIVE)
        gearboxDriver.onBrakeApplied(zeroBrakeThreshold)
        gearboxDriver.onLinearSpeedChange(someLinearSpeed)
    }

    def changeToDriveEcoModeSoftChange(GearboxDriver gearboxDriver) {
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(DRIVE)
        gearboxDriver.onBrakeApplied(zeroBrakeThreshold)
        gearboxDriver.onDriveModeChange(ECO)
        gearboxDriver.onGearChangeMode(softChange)
        gearboxDriver.onLinearSpeedChange(someLinearSpeed)
    }

    def changeToDriveEcoModeHardChange(GearboxDriver gearboxDriver) {
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(DRIVE)
        gearboxDriver.onBrakeApplied(zeroBrakeThreshold)
        gearboxDriver.onDriveModeChange(ECO)
        gearboxDriver.onGearChangeMode(hardChange)
        gearboxDriver.onLinearSpeedChange(someLinearSpeed)
    }

    def changeToDriveSportModeSoftChange(GearboxDriver gearboxDriver) {
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(DRIVE)
        gearboxDriver.onBrakeApplied(zeroBrakeThreshold)
        gearboxDriver.onDriveModeChange(SPORT)
        gearboxDriver.onGearChangeMode(softChange)
        gearboxDriver.onLinearSpeedChange(someLinearSpeed)
    }

    def changeToDriveSportModeHardChange(GearboxDriver gearboxDriver) {
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(DRIVE)
        gearboxDriver.onBrakeApplied(zeroBrakeThreshold)
        gearboxDriver.onDriveModeChange(SPORT)
        gearboxDriver.onGearChangeMode(hardChange)
        gearboxDriver.onLinearSpeedChange(someLinearSpeed)
    }

    def changeToDriveComfortModeSoftChange(GearboxDriver gearboxDriver) {
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(DRIVE)
        gearboxDriver.onBrakeApplied(zeroBrakeThreshold)
        gearboxDriver.onDriveModeChange(COMFORT)
        gearboxDriver.onGearChangeMode(softChange)
        gearboxDriver.onLinearSpeedChange(someLinearSpeed)
    }

    def changeToDriveComfortModeHardChange(GearboxDriver gearboxDriver) {
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(DRIVE)
        gearboxDriver.onBrakeApplied(zeroBrakeThreshold)
        gearboxDriver.onDriveModeChange(COMFORT)
        gearboxDriver.onGearChangeMode(hardChange)
        gearboxDriver.onLinearSpeedChange(someLinearSpeed)
    }

    def changeToManual(GearboxDriver gearboxDriver) {
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(DRIVE)
        gearboxDriver.onLinearSpeedChange(someLinearSpeed)
        gearboxDriver.onGearStickPositionChange(MANUAL)
    }
}