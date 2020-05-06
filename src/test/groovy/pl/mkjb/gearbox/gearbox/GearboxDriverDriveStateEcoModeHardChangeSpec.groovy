package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification
import spock.lang.Unroll

class GearboxDriverDriveStateEcoModeHardChangeSpec extends Specification implements PreparedInput {
    Gearbox gearbox = Mock()
    GearboxDriver gearboxDriver

    def setup() {
        gearboxDriver = GearboxDriver.powerUpGearbox(gearbox)
    }

    @Unroll
    def "should downshift one gear from #input to #output when #throttle.level% throttle engine very low RPM"() {

        given: "fourth gear, eco mode, aggressive mode hard, very low RPM"
        gearbox.currentGear() >> input
        changeToDriveEcoModeHardChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(veryLowRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages third gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | input      | output
        belowKickDownThrottle  | fourthGear | thirdGear
        singleKickDownThrottle | fourthGear | thirdGear
        doubleKickDownThrottle | fourthGear | thirdGear
    }

    @Unroll
    def "should upshift one gear from #input to #output when #throttle.level% throttle engine medium RPM"() {

        given: "third gear, eco mode, aggressive mode hard, medium RPM"
        gearbox.currentGear() >> input
        changeToDriveEcoModeHardChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages forth gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | input     | output
        belowKickDownThrottle  | thirdGear | fourthGear
        singleKickDownThrottle | thirdGear | fourthGear
        doubleKickDownThrottle | thirdGear | fourthGear
    }

    @Unroll
    def "should not change gear when #throttle.level% throttle engine medium RPM"() {

        given: "max gear, eco mode, aggressive mode hard, medium RPM"
        gearbox.currentGear() >> maxGear
        changeToDriveEcoModeHardChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when: "throttle is applied"
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
    def "should not change gear when #throttle.level% throttle engine on low RPM"() {

        given: "fourth gear, eco mode, aggressive mode hard, low RPM"
        gearbox.currentGear() >> fourthGear
        changeToDriveEcoModeHardChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(lowRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages forth gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | output
        belowKickDownThrottle  | fourthGear
        singleKickDownThrottle | fourthGear
        doubleKickDownThrottle | fourthGear
    }

    def "should downshift one gear from #input to #output when braking engine on low RPM"() {

        given: "fourth gear, eco mode, aggressive mode hard, low RPM"
        gearbox.currentGear() >> input
        changeToDriveEcoModeHardChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(lowRpm)

        when: "brake force is applied"
        gearboxDriver.onBrakeApplied(brake)

        then: "gearbox engages third gear"
        1 * gearbox.changeGear(output)

        where:
        brake              | input      | output
        halfBrakeThreshold | fourthGear | thirdGear
    }

    def "should not change gear when braking engine medium RPM"() {

        given: "fourth gear, eco mode, aggressive mode hard, medium RPM"
        gearbox.currentGear() >> fourthGear
        changeToDriveEcoModeHardChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when: "brake force is applied"
        gearboxDriver.onBrakeApplied(brake)

        then: "gearbox engages forth gear"
        1 * gearbox.changeGear(output)

        where:
        brake              | output
        halfBrakeThreshold | fourthGear
    }

    def "should not change gear when braking engine very low RPM"() {

        given: "first gear, eco mode, aggressive mode hard, very low RPM"
        gearbox.currentGear() >> firstGear
        changeToDriveEcoModeHardChange(gearboxDriver)
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
