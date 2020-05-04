package pl.mkjb.gearbox

import groovy.transform.CompileStatic
import pl.mkjb.gearbox.external.shared.BrakeThreshold
import pl.mkjb.gearbox.external.shared.LinearSpeed
import pl.mkjb.gearbox.external.shared.RevGauge
import pl.mkjb.gearbox.external.shared.ThrottleThreshold
import pl.mkjb.gearbox.gearbox.shared.Gear
import pl.mkjb.gearbox.settings.State

import static pl.mkjb.gearbox.settings.Setting.*

@CompileStatic
trait PreparedInput {

    State unsupportedState = State.TEST
    Gear reverseGear = new Gear(REVERSE_GEAR)
    Gear neutralGear = new Gear(NEUTRAL_GEAR)
    Gear firstGear = new Gear(FIRST_GEAR)
    Gear secondGear = new Gear(SECOND_GEAR)

    LinearSpeed zeroLinearSpeed = new LinearSpeed(NO_SPEED)
    LinearSpeed someLinearSpeed = new LinearSpeed(SOME_SPEED)

    BrakeThreshold halfBrakeThreshold = new BrakeThreshold(SOME_THRESHOLD)
    BrakeThreshold zeroBrakeThreshold = new BrakeThreshold(ZERO_THRESHOLD)

    ThrottleThreshold belowKickDownThrottle = new ThrottleThreshold(SOME_THRESHOLD)
    ThrottleThreshold singleKickDownThrottle = new ThrottleThreshold(MORE_THRESHOLD)
    ThrottleThreshold doubleKickDownThrottle = new ThrottleThreshold(MAX_THRESHOLD)

    RevGauge lowRpm = new RevGauge(LOW_RPM)
}