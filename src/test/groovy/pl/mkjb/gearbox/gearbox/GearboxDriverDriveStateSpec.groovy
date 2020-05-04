package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification
import spock.lang.Unroll

import static pl.mkjb.gearbox.settings.Mode.*
import static pl.mkjb.gearbox.settings.State.DRIVE
import static pl.mkjb.gearbox.settings.State.PARK

class GearboxDriverDriveStateSpec extends Specification implements PreparedInput {
    def externalSystem
    def gearboxDriver

    def setup() {
        externalSystem = Stub(ExternalSystem)
        gearboxDriver = GearboxDriver.powerUpGearbox(externalSystem)
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(PARK)
    }

    @Unroll
    def "should change gear to first gear when #input drive mode is active"() {
        when: "gearstick is moved to drive "
        gearboxDriver.onDriveModeChange(input)
        gearboxDriver.onGearStickPositionChange(DRIVE)

        then: "gearbox is in drive state"
        gearboxDriver.checkGearboxState() == DRIVE

        and: "gearbox engages first gear"
        gearboxDriver.checkGearboxGear() == output

        where:
        input      | output
        ECO        | firstGear
        COMFORT    | firstGear
        SPORT      | firstGear
        SPORT_PLUS | firstGear
    }

    @Unroll
    def "should upshift only one gear in eco mode with #input.level% throttle threshold"() {
        when: "eco mode is on, engine is running on lowRpm"
        gearboxDriver.onDriveModeChange(ECO)
        gearboxDriver.onGearStickPositionChange(DRIVE)
        gearboxDriver.onThrottleChange(input)
        externalSystem.onEngineRevsChange(lowRpm)

        then:
        gearboxDriver.checkGearboxGear() == output

        where:
        input                  | output
        belowKickDownThrottle  | secondGear
        singleKickDownThrottle | secondGear
        doubleKickDownThrottle | secondGear
    }
}
