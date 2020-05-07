package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification

import static pl.mkjb.gearbox.settings.State.DRIVE
import static pl.mkjb.gearbox.settings.State.MANUAL

class GearboxStateDrive2ManualSpec extends Specification implements PreparedInput {
    Gearbox gearbox = Stub()
    def gearboxDriver

    def setup() {
        gearboxDriver = GearboxDriver.powerUpGearbox(gearbox)
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(DRIVE)
    }

    def "should not throw exception if state was successfully changed to MANUAL"() {

        given: "vehicle is moving"
        gearboxDriver.onLinearSpeedChange(someLinearSpeed)

        when:
        gearboxDriver.onGearStickPositionChange(MANUAL)

        then:
        noExceptionThrown()
        and:
        gearboxDriver.checkGearboxState() == MANUAL
    }

    def "should throw exception while switching to MANUAL if velocity = 0"() {

        given: "velocity = 0"
        gearboxDriver.onLinearSpeedChange(zeroLinearSpeed)

        when:
        gearboxDriver.onGearStickPositionChange(MANUAL)

        then:
        thrown(IllegalStateException)
    }
}
