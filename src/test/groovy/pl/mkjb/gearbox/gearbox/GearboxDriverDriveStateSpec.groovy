package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification
import spock.lang.Unroll

import static pl.mkjb.gearbox.settings.Mode.*
import static pl.mkjb.gearbox.settings.State.DRIVE

class GearboxDriverDriveStateSpec extends Specification implements PreparedInput {
    Gearbox gearbox = Mock()
    GearboxDriver gearboxDriver

    def setup() {
        gearboxDriver = GearboxDriver.powerUpGearbox(gearbox)
    }

    @Unroll
    def "should always change to first gear no matter which drive mode is active (#drive_mode)"() {

        given: "drive mode is changed from PARK to DRIVE"
        gearbox.currentGear() >> neutralGear
        changeToDrive(gearboxDriver)

        when: "gearstick is moved from PARK to DRIVE"
        gearboxDriver.onDriveModeChange(drive_mode)

        then: "gearbox is in drive state"
        gearboxDriver.checkGearboxState() == DRIVE

        and: "gearbox engages first gear"
        1 * gearbox.changeGear(output)

        where:
        drive_mode | output
        ECO        | firstGear
        COMFORT    | firstGear
        SPORT      | firstGear
        SPORT_PLUS | firstGear
    }

    @Unroll
    def "should downshift only one gear in eco mode when #throttle.level% throttle applied running on very low RPM"() {

        given: "running on fourth gear, eco mode is on, engine is on very low RPM"
        gearbox.currentGear() >> fourthGear
        changeToDrive(gearboxDriver)
        gearboxDriver.onDriveModeChange(ECO)
        gearboxDriver.onEngineRevsChange(veryLowRpm)

        when: "throttle is changing"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages third gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | output
        belowKickDownThrottle  | thirdGear
        singleKickDownThrottle | thirdGear
        doubleKickDownThrottle | thirdGear
    }

    @Unroll
    def "should upshift one gear in eco mode when #throttle.level% throttle applied running on medium RPM"() {

        given: "running on third gear, eco mode is on, engine is on medium RPM"
        gearbox.currentGear() >> thirdGear
        changeToDrive(gearboxDriver)
        gearboxDriver.onDriveModeChange(ECO)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when: "throttle is changing"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages forth gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | output
        belowKickDownThrottle  | fourthGear
        singleKickDownThrottle | fourthGear
        doubleKickDownThrottle | fourthGear
    }

    @Unroll
    def "should not change gear in eco mode when #throttle.level% throttle applied running on medium RPM"() {

        given: "running on max gear, eco mode is on, engine is on medium RPM"
        gearbox.currentGear() >> maxGear
        changeToDrive(gearboxDriver)
        gearboxDriver.onDriveModeChange(ECO)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when: "throttle is changing"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages forth gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | output
        belowKickDownThrottle  | maxGear
        singleKickDownThrottle | maxGear
        doubleKickDownThrottle | maxGear
    }

    @Unroll
    def "should not change gear in eco mode when #throttle.level% throttle applied running on low RPM"() {

        given: "running on fourth gear, eco mode is on, engine is on low RPM"
        gearbox.currentGear() >> fourthGear
        changeToDrive(gearboxDriver)
        gearboxDriver.onDriveModeChange(ECO)
        gearboxDriver.onEngineRevsChange(lowRpm)

        when: "throttle is changing"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages forth gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | output
        belowKickDownThrottle  | fourthGear
        singleKickDownThrottle | fourthGear
        doubleKickDownThrottle | fourthGear
    }

    def "should downshift one gear in eco mode when brake force is applied running on low RPM"() {

        given: "running on fourth gear, eco mode is on, engine is on low RPM"
        gearbox.currentGear() >> fourthGear
        changeToDrive(gearboxDriver)
        gearboxDriver.onDriveModeChange(ECO)
        gearboxDriver.onEngineRevsChange(lowRpm)

        when: "brake force is applied"
        gearboxDriver.onBrakeApplied(brake)

        then: "gearbox engages third gear"
        1 * gearbox.changeGear(output)

        where:
        brake              | output
        halfBrakeThreshold | thirdGear
    }

    def "should not change gear in eco mode when brake force is applied running on medium RPM"() {

        given: "running on fourth gear, eco mode is on, engine is on medium RPM"
        gearbox.currentGear() >> fourthGear
        changeToDrive(gearboxDriver)
        gearboxDriver.onDriveModeChange(ECO)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when: "brake force is applied"
        gearboxDriver.onBrakeApplied(brake)

        then: "gearbox engages forth gear"
        1 * gearbox.changeGear(output)

        where:
        brake              | output
        halfBrakeThreshold | fourthGear
    }

    def "should not change gear in eco mode when brake force is applied running on very low RPM"() {

        given: "running on first gear, eco mode is on, engine is on very low RPM"
        gearbox.currentGear() >> firstGear
        changeToDrive(gearboxDriver)
        gearboxDriver.onDriveModeChange(ECO)
        gearboxDriver.onEngineRevsChange(veryLowRpm)

        when: "brake force is applied"
        gearboxDriver.onBrakeApplied(brake)

        then: "gearbox stays in first gear"
        1 * gearbox.changeGear(output)

        where:
        brake              | output
        halfBrakeThreshold | firstGear
    }
}
