package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification
import spock.lang.Unroll

import static pl.mkjb.gearbox.settings.Mode.*
import static pl.mkjb.gearbox.settings.State.DRIVE

class GearboxDriverDriveStateSpec extends Specification implements PreparedInput {
    Gearbox gearbox = Mock()
    GearboxDriver gearboxDriver

    def setup() {
        gearboxDriver = GearboxDriver.powerUpGearbox(gearbox)
    }

    @Unroll
    def "should always change to first gear no matter which drive mode is active (#drive_mode)"() {

        given: "drive mode is changed from PARK to DRIVE"
        gearbox.currentGear() >> neutralGear
        changeToDrive(gearboxDriver)

        when: "gearstick is moved from PARK to DRIVE"
        gearboxDriver.onDriveModeChange(drive_mode)

        then: "gearbox is in drive state"
        gearboxDriver.checkGearboxState() == DRIVE

        and: "gearbox engages first gear"
        1 * gearbox.changeGear(expected_gear)

        where:
        drive_mode | expected_gear
        ECO        | firstGear
        COMFORT    | firstGear
        SPORT      | firstGear
    }

}
