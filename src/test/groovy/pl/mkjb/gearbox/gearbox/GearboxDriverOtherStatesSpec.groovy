package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification
import spock.lang.Unroll

import static pl.mkjb.gearbox.settings.Mode.*
import static pl.mkjb.gearbox.settings.State.*

class GearboxDriverOtherStatesSpec extends Specification implements PreparedInput {
    Gearbox gearbox = Mock()
    def gearboxDriver

    def setup() {
        gearbox.currentGear() >> neutralGear
        gearboxDriver = GearboxDriver.powerUpGearbox(gearbox)
        gearboxDriver.onBrakeApplied(halfBrakeThreshold)
        gearboxDriver.onGearStickPositionChange(PARK)
    }

    @Unroll
    def "should change gear to reverse no matter which drive mode is active (#drive_mode)"() {

        given: "applying drive mode"
        gearboxDriver.onDriveModeChange(drive_mode)

        when: "gearstick is moved to reverse"
        gearboxDriver.onGearStickPositionChange(REVERSE)

        then: "gearbox is in reverse state"
        gearboxDriver.checkGearboxState() == REVERSE

        and: "gearbox engages reverse gear"
        1 * gearbox.changeGear(expected_gear)

        where:
        drive_mode | expected_gear
        ECO        | reverseGear
        COMFORT    | reverseGear
        SPORT      | reverseGear
    }

    @Unroll
    def "should change gear to neutral no matter which drive mode is active (#drive_mode)"() {

        given: "applying drive mode"
        gearboxDriver.onDriveModeChange(drive_mode)

        when: "gearstick is moved to neutral"
        gearboxDriver.onGearStickPositionChange(NEUTRAL)

        then: "gearbox is in neutral state"
        gearboxDriver.checkGearboxState() == NEUTRAL

        and: "gearbox engages neutral gear"
        1 * gearbox.changeGear(expected_gear)

        where:
        drive_mode | expected_gear
        ECO        | neutralGear
        COMFORT    | neutralGear
        SPORT      | neutralGear
    }

    @Unroll
    def "should change gear to park no matter which drive mode is active (#drive_mode)"() {

        given: "applying drive mode"
        gearboxDriver.onDriveModeChange(drive_mode)

        when: "gearstick is moved to park"
        gearboxDriver.onGearStickPositionChange(PARK)

        then: "gearbox is in park state"
        gearboxDriver.checkGearboxState() == PARK

        and: "gearbox engages neutral gear"
        1 * gearbox.changeGear(expected_gear)

        where:
        drive_mode | expected_gear
        ECO        | neutralGear
        COMFORT    | neutralGear
        SPORT      | neutralGear
    }
}
