package pl.mkjb.gearbox

import groovy.transform.CompileStatic
import pl.mkjb.gearbox.external.shared.BrakeThreshold
import pl.mkjb.gearbox.external.shared.LinearSpeed
import pl.mkjb.gearbox.external.shared.RevGauge
import pl.mkjb.gearbox.external.shared.ThrottleThreshold
import pl.mkjb.gearbox.gearbox.GearboxDriver
import pl.mkjb.gearbox.gearbox.shared.Gear
import pl.mkjb.gearbox.settings.State

import static pl.mkjb.gearbox.settings.Setting.*
import static pl.mkjb.gearbox.settings.State.DRIVE

@CompileStatic
trait PreparedInput {

    State unsupportedState = State.TEST
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

    def changeToDrive(GearboxDriver gearboxDriver) {
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(DRIVE)
        gearboxDriver.onBrakeApplied(zeroBrakeThreshold)
    }
}