package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification

import static pl.mkjb.gearbox.settings.State.PARK

class GearboxStateSpec extends Specification implements PreparedInput {
    Gearbox gearbox = Stub()
    def gearboxDriver

    def setup() {
        gearboxDriver = GearboxDriver.powerUpGearbox(gearbox)
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(PARK)
    }

    def "should throw exception when changing to unknown state"() {

        when:
        gearboxDriver.onGearStickPositionChange(unsupportedState)

        then:
        def ex = thrown(IllegalArgumentException)
        and:
        ex.message == "Unknown gearbox state"
    }

}
