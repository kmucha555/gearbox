package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification

import static pl.mkjb.gearbox.settings.State.PARK
import static pl.mkjb.gearbox.settings.State.REVERSE

class GearboxStatePark2ReverseSpec extends Specification implements PreparedInput {
    def gearboxDriver

    def setup() {
        gearboxDriver = GearboxDriver.powerUpGearbox()
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(PARK)
    }

    def "should not throw exception if states was successfully changed to REVERSE"() {

        given: "some brake force is applied"
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)

        when:
        gearboxDriver.onGearStickPositionChange(REVERSE)

        then:
        noExceptionThrown()
        and:
        gearboxDriver.checkGearboxState() == REVERSE
    }

    def "should throw exception when changing PARK to REVERSE"() {

        given: "no brake force applied"
        gearboxDriver.onBrakeApplied(zeroBrakeThreshold)

        when:
        gearboxDriver.onGearStickPositionChange(REVERSE)

        then:
        thrown(IllegalStateException)
        and:
        gearboxDriver.checkGearboxState() == PARK
    }
}
