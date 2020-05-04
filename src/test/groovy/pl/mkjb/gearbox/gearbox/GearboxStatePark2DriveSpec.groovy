package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification

import static pl.mkjb.gearbox.settings.State.DRIVE
import static pl.mkjb.gearbox.settings.State.PARK

class GearboxStatePark2DriveSpec extends Specification implements PreparedInput {
    def gearboxDriver

    def setup() {
        gearboxDriver = GearboxDriver.powerUpGearbox()
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(PARK)
    }

    def "should not throw exception if states was successfully changed to DRIVE"() {

        given: "some brake force is applied"
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)

        when:
        gearboxDriver.onGearStickPositionChange(DRIVE)

        then:
        noExceptionThrown()
        and:
        gearboxDriver.checkGearboxState() == DRIVE
    }

    def "should throw exception when changing PARK to DRIVE"() {

        when: "no brake force applied"
        gearboxDriver.onBrakeApplied(zeroBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(DRIVE)

        then:
        thrown(IllegalStateException)
    }
}
