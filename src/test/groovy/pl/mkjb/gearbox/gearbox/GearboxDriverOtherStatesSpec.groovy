package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification

import static pl.mkjb.gearbox.settings.Mode.*
import static pl.mkjb.gearbox.settings.State.*

class GearboxDriverOtherStatesSpec extends Specification implements PreparedInput {
    Gearbox gearbox = Mock()
    def gearboxDriver

    def setup(){
        gearboxDriver = GearboxDriver.powerUpGearbox(gearbox)
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(PARK)
    }

    def "should change gear to reverse no matter which drive mode is active"() {

        given: "applying drive mode"
        gearboxDriver.onDriveModeChange(input)

        when: "gearstick is moved to reverse"
        gearboxDriver.onGearStickPositionChange(REVERSE)

        then: "gearbox is in reverse state"
        gearboxDriver.checkGearboxState() == REVERSE

        and: "gearbox engages reverse gear"
        1 * gearbox.changeGear(output)

        where:
        input      | output
        ECO        | reverseGear
        COMFORT    | reverseGear
        SPORT      | reverseGear
    }

    def "should change gear to neutral no matter which drive mode is active"() {

        given: "applying drive mode"
        gearboxDriver.onDriveModeChange(input)

        when: "gearstick is moved to neutral"
        gearboxDriver.onGearStickPositionChange(NEUTRAL)

        then: "gearbox is in neutral state"
        gearboxDriver.checkGearboxState() == NEUTRAL

        and: "gearbox engages neutral gear"
        1 * gearbox.changeGear(output)

        where:
        input      | output
        ECO        | neutralGear
        COMFORT    | neutralGear
        SPORT      | neutralGear
    }

    def "should change gear to park no matter which drive mode is active"() {

        given: "applying drive mode"
        gearboxDriver.onDriveModeChange(input)

        when: "gearstick is moved to park"
        gearboxDriver.onGearStickPositionChange(PARK)

        then: "gearbox is in park state"
        gearboxDriver.checkGearboxState() == PARK

        and: "gearbox engages neutral gear"
        1 * gearbox.changeGear(output)

        where:
        input      | output
        ECO        | neutralGear
        COMFORT    | neutralGear
        SPORT      | neutralGear
    }
}
