package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification
import spock.lang.Unroll

class GearboxDriverDriveStateSportModeSoftChangeSpec extends Specification implements PreparedInput {
    Gearbox gearbox = Mock()
    GearboxDriver gearboxDriver

    def setup() {
        gearboxDriver = GearboxDriver.powerUpGearbox(gearbox)
    }

    @Unroll
    def "should kickdown from #current_gear to #expected_gear when #throttle throttle, engine #engine_rpm RPM"() {
        given: "drive mode sport, gear change mode soft"
        gearbox.currentGear() >> current_gear
        changeToDriveSportModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages expected gear"
        1 * gearbox.changeGear(expected_gear)

        where:
        throttle               | engine_rpm | current_gear | expected_gear
        singleKickDownThrottle | veryLowRpm | thirdGear    | secondGear
        singleKickDownThrottle | lowRpm     | thirdGear    | secondGear
        singleKickDownThrottle | mediumRpm  | thirdGear    | secondGear
        singleKickDownThrottle | highRpm    | thirdGear    | secondGear

        doubleKickDownThrottle | veryLowRpm | fourthGear   | secondGear
        doubleKickDownThrottle | veryLowRpm | secondGear   | firstGear
        doubleKickDownThrottle | lowRpm     | fourthGear   | secondGear
        doubleKickDownThrottle | lowRpm     | secondGear   | firstGear
        doubleKickDownThrottle | mediumRpm  | fourthGear   | secondGear
        doubleKickDownThrottle | mediumRpm  | secondGear   | firstGear
        doubleKickDownThrottle | highRpm    | secondGear   | firstGear
        doubleKickDownThrottle | highRpm    | fourthGear   | secondGear
    }

    @Unroll
    def "should downshift from #current_gear to #expected_gear when #throttle throttle, engine #engine_rpm RPM"() {

        given: "drive mode sport, gear change mode soft"
        gearbox.currentGear() >> current_gear
        changeToDriveSportModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages expected gear"
        1 * gearbox.changeGear(expected_gear)

        where:
        throttle               | engine_rpm | current_gear | expected_gear
        belowKickDownThrottle  | veryLowRpm | fourthGear   | thirdGear
        belowKickDownThrottle  | veryLowRpm | secondGear   | firstGear
        belowKickDownThrottle  | lowRpm     | fourthGear   | thirdGear
        belowKickDownThrottle  | lowRpm     | secondGear   | firstGear

        singleKickDownThrottle | veryLowRpm | fourthGear   | thirdGear
        singleKickDownThrottle | veryLowRpm | secondGear   | firstGear
        singleKickDownThrottle | lowRpm     | secondGear   | firstGear
        singleKickDownThrottle | lowRpm     | fourthGear   | thirdGear
        singleKickDownThrottle | mediumRpm  | thirdGear    | secondGear
        singleKickDownThrottle | mediumRpm  | secondGear   | firstGear

        doubleKickDownThrottle | veryLowRpm | fourthGear   | secondGear
        doubleKickDownThrottle | veryLowRpm | secondGear   | firstGear
        doubleKickDownThrottle | lowRpm     | fourthGear   | secondGear
        doubleKickDownThrottle | lowRpm     | secondGear   | firstGear
        doubleKickDownThrottle | mediumRpm  | fourthGear   | secondGear
        doubleKickDownThrottle | mediumRpm  | secondGear   | firstGear
    }

    @Unroll
    def "should not downshift from #current_gear when #throttle throttle, engine #engine_rpm RPM"() {

        given: "drive mode sport, gear change mode soft"
        gearbox.currentGear() >> current_gear
        changeToDriveSportModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox stays in expected gear"
        1 * gearbox.changeGear(expected_gear)

        where:
        throttle               | engine_rpm | current_gear | expected_gear
        belowKickDownThrottle  | veryLowRpm | firstGear    | firstGear
        belowKickDownThrottle  | lowRpm     | firstGear    | firstGear

        singleKickDownThrottle | veryLowRpm | firstGear    | firstGear
        singleKickDownThrottle | lowRpm     | firstGear    | firstGear
        singleKickDownThrottle | mediumRpm  | firstGear    | firstGear

        doubleKickDownThrottle | veryLowRpm | firstGear    | firstGear
        doubleKickDownThrottle | lowRpm     | firstGear    | firstGear
        doubleKickDownThrottle | mediumRpm  | firstGear    | firstGear
    }

    @Unroll
    def "should not downshift from #current_gear when #brake braking, engine #engine_rpm RPM"() {

        given: "drive mode sport, gear change mode soft"
        gearbox.currentGear() >> current_gear
        changeToDriveSportModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "brake force applied"
        gearboxDriver.onBrakeApplied(brake)

        then: "gearbox stays in expected gear"
        1 * gearbox.changeGear(expected_gear)

        where:
        brake              | engine_rpm  | current_gear | expected_gear
        halfBrakeThreshold | veryLowRpm  | firstGear    | firstGear

        halfBrakeThreshold | lowRpm      | firstGear    | firstGear

        halfBrakeThreshold | mediumRpm   | firstGear    | firstGear

        halfBrakeThreshold | highRpm     | fourthGear   | fourthGear
        halfBrakeThreshold | highRpm     | firstGear    | firstGear

        halfBrakeThreshold | veryHighRpm | fourthGear   | fourthGear
        halfBrakeThreshold | veryHighRpm | firstGear    | firstGear
    }

    @Unroll
    def "should upshift from #current_gear to #expected_gear when #throttle throttle, engine #engine_rpm RPM"() {

        given: "drive mode sport, gear change mode soft"
        gearbox.currentGear() >> current_gear
        changeToDriveSportModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages expected gear"
        1 * gearbox.changeGear(expected_gear)

        where:
        throttle               | engine_rpm  | current_gear | expected_gear
        belowKickDownThrottle  | highRpm     | thirdGear    | fourthGear
        belowKickDownThrottle  | veryHighRpm | thirdGear    | fourthGear
        belowKickDownThrottle  | maxRpm      | thirdGear    | fourthGear

        singleKickDownThrottle | veryHighRpm | thirdGear    | fourthGear
        singleKickDownThrottle | maxRpm      | thirdGear    | fourthGear

        doubleKickDownThrottle | veryHighRpm | thirdGear    | fourthGear
        doubleKickDownThrottle | maxRpm      | thirdGear    | fourthGear
    }

    @Unroll
    def "should not upshift from #current_gear when #throttle throttle, engine #engine_rpm RPM"() {

        given: "drive mode sport, gear change mode soft"
        gearbox.currentGear() >> current_gear
        changeToDriveSportModeSoftChange(gearboxDriver)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "throttle is applied"
        gearboxDriver.onThrottleChange(throttle)

        then: "gearbox engages expected gear"
        1 * gearbox.changeGear(expected_gear)

        where:
        throttle              | engine_rpm  | current_gear | expected_gear
        belowKickDownThrottle | mediumRpm   | maxGear      | maxGear
        belowKickDownThrottle | highRpm     | maxGear      | maxGear
        belowKickDownThrottle | veryHighRpm | maxGear      | maxGear
        belowKickDownThrottle | maxRpm      | maxGear      | maxGear
    }
}
