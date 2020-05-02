package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification

import static pl.mkjb.gearbox.settings.State.DRIVE
import static pl.mkjb.gearbox.settings.State.PARK

class GearboxStateDrive2ParkSpec extends Specification implements PreparedInput {
    def externalSystem
    def gearboxDriver

    def setup() {
        externalSystem = Stub(ExternalSystem)
        gearboxDriver = GearboxDriver.powerUpGearbox(externalSystem)
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(DRIVE)
    }

    def "should not throw exception if state was successfully changed to PARK"() {

        given: "some brake force is applied, zero velocity"
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        externalSystem.getLinearSpeed() >> zeroLinearSpeed

        when:
        gearboxDriver.onGearStickPositionChange(PARK)

        then:
        noExceptionThrown()
        and:
        gearboxDriver.checkGearboxState() == PARK
    }

    def "should throw exception while switching to PARK if velocity > 0 and brake > 0"() {

        given: "some brake force is applied, velocity > 0"
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        externalSystem.getLinearSpeed() >> someLinearSpeed

        when:
        gearboxDriver.onGearStickPositionChange(PARK)

        then:
        thrown(IllegalStateException)
    }

    def "should throw exception while switching to PARK if brake = 0 and velocity = 0"() {

        given: "no brake force is applied, velocity = 0"
        gearboxDriver.onBrakeApplied(zeroBrakeThreshold)
        externalSystem.getLinearSpeed() >> zeroLinearSpeed

        when:
        gearboxDriver.onGearStickPositionChange(PARK)

        then:
        thrown(IllegalStateException)
    }

    def "should throw exception while switching to PARK if brake = 0 and velocity > 0"() {

        given: "no brake force is applied, velocity > 0"
        gearboxDriver.onBrakeApplied(zeroBrakeThreshold)
        externalSystem.getLinearSpeed() >> someLinearSpeed

        when:
        gearboxDriver.onGearStickPositionChange(PARK)

        then:
        thrown(IllegalStateException)
    }
}
