package pl.mkjb.gearbox

import groovy.transform.CompileStatic
import pl.mkjb.gearbox.external.shared.BrakeThreshold
import pl.mkjb.gearbox.external.shared.LinearSpeed
import pl.mkjb.gearbox.settings.State

import static pl.mkjb.gearbox.settings.Setting.*

@CompileStatic
trait PreparedInput {

    State unsupportedState = State.TEST

    LinearSpeed zeroLinearSpeed = new LinearSpeed(NO_SPEED)
    LinearSpeed someLinearSpeed = new LinearSpeed(SOME_SPEED)

    BrakeThreshold halfBrakeThreshold = new BrakeThreshold(HALF_THRESHOLD)
    BrakeThreshold zeroBrakeThreshold = new BrakeThreshold(ZERO_THRESHOLD)
}