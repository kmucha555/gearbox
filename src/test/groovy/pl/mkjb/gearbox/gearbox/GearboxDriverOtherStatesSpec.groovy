package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification

import static pl.mkjb.gearbox.settings.Mode.COMFORT
import static pl.mkjb.gearbox.settings.Mode.ECO
import static pl.mkjb.gearbox.settings.Mode.SPORT
import static pl.mkjb.gearbox.settings.Mode.SPORT_PLUS
import static pl.mkjb.gearbox.settings.State.*

class GearboxDriverOtherStatesSpec extends Specification implements PreparedInput {
    def externalSystem
    def gearboxDriver

    def setup(){
        externalSystem = Stub(ExternalSystem)
        gearboxDriver = GearboxDriver.powerUpGearbox(externalSystem)
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(PARK)
    }

    def "should change gear to reverse no matter which drive mode is active"() {
        when: "gearstick is moved to reverse"
        gearboxDriver.onDriveModeChange(input)
        gearboxDriver.onGearStickPositionChange(REVERSE)

        then: "gearbox is in reverse state"
        gearboxDriver.checkGearboxState() == REVERSE

        and: "gearbox engages reverse gear"
        gearboxDriver.checkGearboxGear() == output

        where:
        input      | output
        ECO        | reverseGear
        COMFORT    | reverseGear
        SPORT      | reverseGear
        SPORT_PLUS | reverseGear
    }

    def "should change gear to neutral no matter which drive mode is active"() {
        when: "gearstick is moved to neutral"
        gearboxDriver.onDriveModeChange(input)
        gearboxDriver.onGearStickPositionChange(NEUTRAL)

        then: "gearbox is in neutral state"
        gearboxDriver.checkGearboxState() == NEUTRAL

        and: "gearbox engages neutral gear"
        gearboxDriver.checkGearboxGear() == output

        where:
        input      | output
        ECO        | neutralGear
        COMFORT    | neutralGear
        SPORT      | neutralGear
        SPORT_PLUS | neutralGear
    }

    def "should change gear to park no matter which drive mode is active"() {
        when: "gearstick is moved to park"
        gearboxDriver.onDriveModeChange(input)
        gearboxDriver.onGearStickPositionChange(PARK)

        then: "gearbox is in park state"
        gearboxDriver.checkGearboxState() == PARK

        and: "gearbox engages neutral gear"
        gearboxDriver.checkGearboxGear() == neutralGear

        where:
        input      | output
        ECO        | neutralGear
        COMFORT    | neutralGear
        SPORT      | neutralGear
        SPORT_PLUS | neutralGear
    }
}
