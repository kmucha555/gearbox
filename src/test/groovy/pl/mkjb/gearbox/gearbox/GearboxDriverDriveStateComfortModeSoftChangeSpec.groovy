package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification
import spock.lang.Unroll

class GearboxDriverDriveStateComfortModeSoftChangeSpec extends Specification implements PreparedInput {
    Gearbox gearbox = Mock()
    GearboxDriver gearboxDriver

    def setup() {
        gearboxDriver = GearboxDriver.powerUpGearbox(gearbox)
    }

    @Unroll
    def "should upshift one gear from #input to #output when #throttle% throttle engine medium RPM"() {

        given: "third gear, comfort mode, aggressive mode soft, medium RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages fourth gear"
        1 * gearbox.changeGear(output)

        where:
        throttle              | input     | output
        belowKickDownThrottle | thirdGear | fourthGear
    }

    @Unroll
    def "should downshift one gear from #input to #output when #throttle% throttle engine medium RPM"() {

        given: "third gear, comfort mode, aggressive mode soft, medium RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages second gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | input     | output
        singleKickDownThrottle | thirdGear | secondGear
        doubleKickDownThrottle | thirdGear | secondGear
    }

    @Unroll
    def "should downshift one gear from #input to #output when #throttle% throttle engine low RPM"() {

        given: "fourth gear, comfort mode, aggressive mode soft, low RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(lowRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages third gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | input      | output
        singleKickDownThrottle | fourthGear | thirdGear
        doubleKickDownThrottle | fourthGear | thirdGear
    }

    @Unroll
    def "should not upshift one gear from #input to #output when #throttle% throttle engine low RPM"() {

        given: "third gear, comfort mode, aggressive mode soft, low RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(lowRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages fourth gear"
        1 * gearbox.changeGear(output)

        where:
        throttle              | input     | output
        belowKickDownThrottle | thirdGear | thirdGear
    }

    @Unroll
    def "should downshift one gear from #input to #output when #throttle% throttle engine very low RPM"() {

        given: "fourth gear, comfort mode, aggressive mode soft, very low RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(veryLowRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages second gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | input      | output
        belowKickDownThrottle  | fourthGear | thirdGear
        singleKickDownThrottle | fourthGear | thirdGear
        doubleKickDownThrottle | fourthGear | thirdGear
    }

    @Unroll
    def "should not downshift from first gear when #throttle% throttle engine very low RPM"() {

        given: "first gear, comfort mode, aggressive mode soft, very low RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(veryLowRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox stays in first gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | input     | output
        belowKickDownThrottle  | firstGear | firstGear
        singleKickDownThrottle | firstGear | firstGear
        doubleKickDownThrottle | firstGear | firstGear
    }

    @Unroll
    def "should downshift only one gear when running on second gear #throttle% throttle engine very low RPM"() {

        given: "second gear, comfort mode, aggressive mode soft, very low RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(veryLowRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages first gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | input      | output
        belowKickDownThrottle  | secondGear | firstGear
        singleKickDownThrottle | secondGear | firstGear
        doubleKickDownThrottle | secondGear | firstGear
    }

    @Unroll
    def "should downshift only one gear when running on second gear #throttle% throttle engine low RPM"() {

        given: "second gear, comfort mode, aggressive mode soft, low RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(lowRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages first gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | input      | output
        singleKickDownThrottle | secondGear | firstGear
        doubleKickDownThrottle | secondGear | firstGear
    }

    @Unroll
    def "should downshift only one gear when running on second gear #throttle% throttle engine medium RPM"() {

        given: "second gear, comfort mode, aggressive mode soft, medium RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages first gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | input      | output
        singleKickDownThrottle | secondGear | firstGear
        doubleKickDownThrottle | secondGear | firstGear
    }

    @Unroll
    def "should upshift one gear from #input to #output when #throttle% throttle engine high RPM"() {

        given: "third gear, comfort mode, aggressive mode soft, high RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(highRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages fourth gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | input     | output
        belowKickDownThrottle  | thirdGear | fourthGear
        singleKickDownThrottle | thirdGear | fourthGear
        doubleKickDownThrottle | thirdGear | fourthGear
    }

    @Unroll
    def "should not upshift when on max gear #throttle% throttle engine high RPM"() {

        given: "max gear, comfort mode, aggressive mode soft, high RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(highRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages max gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | input   | output
        belowKickDownThrottle  | maxGear | maxGear
        singleKickDownThrottle | maxGear | maxGear
        doubleKickDownThrottle | maxGear | maxGear
    }

    @Unroll
    def "should kickdown from #input to #output when #throttle% throttle engine very high RPM"() {

        given: "third gear, comfort mode, aggressive mode soft, very high RPM"
        gearbox.currentGear() >> input
        changeToDriveSportModeHardChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(veryHighRpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages fourth gear"
        1 * gearbox.changeGear(output)

        where:
        throttle               | input     | output
        singleKickDownThrottle | thirdGear | secondGear
        doubleKickDownThrottle | thirdGear | firstGear
    }

    @Unroll
    def "should not downshift when braking engine medium RPM"() {

        given: "fourth gear, comfort mode, aggressive mode soft, medium RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when: "brake force applied"
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)

        then: "gearbox stays in fourth gear"
        1 * gearbox.changeGear(output)

        where:
        throttle           | input      | output
        halfBrakeThreshold | fourthGear | fourthGear
    }

    @Unroll
    def "should downshift one gear from #input to #output when braking engine low RPM"() {

        given: "fourth gear, comfort mode, aggressive mode soft, medium RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(lowRpm)

        when: "brake force applied"
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)

        then: "gearbox engages third gear"
        1 * gearbox.changeGear(output)

        where:
        throttle           | input      | output
        halfBrakeThreshold | fourthGear | thirdGear
    }

    @Unroll
    def "should downshift one gear from #input to #output when braking engine very low RPM"() {

        given: "fourth gear, comfort mode, aggressive mode soft, very low RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(veryLowRpm)

        when: "brake force applied"
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)

        then: "gearbox engages third gear"
        1 * gearbox.changeGear(output)

        where:
        throttle           | input      | output
        halfBrakeThreshold | fourthGear | thirdGear
    }

    @Unroll
    def "should not downshift running on first gear when braking engine medium RPM"() {

        given: "first gear, comfort mode, aggressive mode soft, medium RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when: "brake force applied"
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)

        then: "gearbox engages first gear"
        1 * gearbox.changeGear(output)

        where:
        throttle           | input     | output
        halfBrakeThreshold | firstGear | firstGear
    }

    @Unroll
    def "should not downshift when braking engine high RPM"() {

        given: "fourth gear, comfort mode, aggressive mode soft, high RPM"
        gearbox.currentGear() >> input
        changeToDriveComfortModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(highRpm)

        when: "brake force applied"
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)

        then: "gearbox engages fourth gear"
        1 * gearbox.changeGear(output)

        where:
        throttle           | input      | output
        halfBrakeThreshold | fourthGear | fourthGear
    }
}
