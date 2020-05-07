package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification
import spock.lang.Unroll

import static pl.mkjb.gearbox.settings.AggressiveMode.*
import static pl.mkjb.gearbox.settings.Mode.*
import static pl.mkjb.gearbox.settings.State.MANUAL

class GearboxDriverPaddleSpec extends Specification implements PreparedInput {
    Gearbox gearbox = Mock()
    GearboxDriver gearboxDriver

    def setup() {
        gearboxDriver = GearboxDriver.powerUpGearbox(gearbox)
    }

    def "should change gearbox state to MANUAL when paddle is used no matter which drive mode is used (#drive_mode)"() {

        given: "gearbox in drive mode on third gear"
        gearbox.currentGear() >> current_gear
        changeToDrive(gearboxDriver)
        gearboxDriver.onDriveModeChange(drive_mode)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "paddle is used"
        gearboxDriver.onPaddleUse(upshift)

        then: "gearbox state is switched to manual"
        gearboxDriver.checkGearboxState() == expected_state
        and:
        1 * gearbox.changeGear(_)

        where:
        drive_mode | current_gear | engine_rpm | expected_state
        ECO        | thirdGear    | mediumRpm  | MANUAL
        COMFORT    | thirdGear    | mediumRpm  | MANUAL
        SPORT      | thirdGear    | mediumRpm  | MANUAL
    }

    def "should change gearbox state to MANUAL when paddle is used no matter which aggressive mode is used (#aggressive_mode)"() {

        given: "gearbox in drive mode on third gear"
        gearbox.currentGear() >> current_gear
        changeToDrive(gearboxDriver)
        gearboxDriver.onGearChangeMode(aggressive_mode)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "paddle is used"
        gearboxDriver.onPaddleUse(upshift)

        then: "gearbox state is switched to manual"
        gearboxDriver.checkGearboxState() == expected_gear

        where:
        aggressive_mode | current_gear | engine_rpm | expected_gear
        SOFT            | thirdGear    | mediumRpm  | MANUAL
        HARD            | thirdGear    | mediumRpm  | MANUAL
        EXTREME         | thirdGear    | mediumRpm  | MANUAL
    }

    @Unroll
    def "should upshift from #current_gear to #expected_gear, engine #engine_rpm"() {

        given: "vehicle is in drive"
        gearbox.currentGear() >> current_gear
        changeToDrive(gearboxDriver)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "paddle is used gearbox is switching to manual"
        gearboxDriver.onPaddleUse(upshift)

        then:
        1 * gearbox.changeGear(expected_gear)

        where:
        current_gear | engine_rpm  | expected_gear
        firstGear    | lowRpm      | secondGear
        secondGear   | lowRpm      | thirdGear
        firstGear    | mediumRpm   | secondGear
        secondGear   | mediumRpm   | thirdGear
        firstGear    | highRpm     | secondGear
        secondGear   | highRpm     | thirdGear
        firstGear    | veryHighRpm | secondGear
        secondGear   | veryHighRpm | thirdGear
    }

    @Unroll
    def "should not upshift from #current_gear to #expected_gear, engine #engine_rpm"() {

        given: "vehicle is in drive"
        gearbox.currentGear() >> current_gear
        changeToDrive(gearboxDriver)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "paddle is used gearbox is switching to manual"
        gearboxDriver.onPaddleUse(upshift)

        then:
        1 * gearbox.changeGear(expected_gear)

        where:
        current_gear | engine_rpm | expected_gear
        firstGear    | veryLowRpm | firstGear
        thirdGear    | veryLowRpm | thirdGear
    }

    @Unroll
    def "should not upshift when gearbox is on max #current_gear, engine #engine_rpm"() {

        given: "vehicle is in drive"
        gearbox.currentGear() >> current_gear
        changeToDrive(gearboxDriver)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "paddle is used gearbox is switching to manual"
        gearboxDriver.onPaddleUse(upshift)

        then:
        1 * gearbox.changeGear(expected_gear)

        where:
        current_gear | engine_rpm  | expected_gear
        maxGear      | veryLowRpm  | maxGear
        maxGear      | lowRpm      | maxGear
        maxGear      | mediumRpm   | maxGear
        maxGear      | highRpm     | maxGear
        maxGear      | veryHighRpm | maxGear
    }

    @Unroll
    def "should downshift from #current_gear to #expected_gear, engine #engine_rpm"() {

        given: "vehicle is in drive"
        gearbox.currentGear() >> current_gear
        changeToDrive(gearboxDriver)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "paddle is used gearbox is switching to manual"
        gearboxDriver.onPaddleUse(downshift)

        then:
        1 * gearbox.changeGear(expected_gear)

        where:
        current_gear | engine_rpm | expected_gear
        secondGear   | veryLowRpm | firstGear
        fourthGear   | veryLowRpm | thirdGear
        secondGear   | lowRpm     | firstGear
        fourthGear   | lowRpm     | thirdGear
        secondGear   | mediumRpm  | firstGear
        fourthGear   | mediumRpm  | thirdGear
        secondGear   | highRpm    | firstGear
        fourthGear   | highRpm    | thirdGear
    }

    @Unroll
    def "should not downshift from #current_gear to #expected_gear, engine #engine_rpm"() {

        given: "vehicle is in drive"
        gearbox.currentGear() >> current_gear
        changeToDrive(gearboxDriver)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "paddle is used gearbox is switching to manual"
        gearboxDriver.onPaddleUse(downshift)

        then:
        1 * gearbox.changeGear(expected_gear)

        where:
        current_gear | engine_rpm  | expected_gear
        fourthGear   | veryHighRpm | fourthGear
        secondGear   | veryHighRpm | secondGear
    }

    def "should not downshift when gearbox is on first gear, engine #engine_rpm"() {

        given: "vehicle is in drive"
        gearbox.currentGear() >> current_gear
        changeToDrive(gearboxDriver)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when: "paddle is used gearbox is switching to manual"
        gearboxDriver.onPaddleUse(downshift)

        then:
        1 * gearbox.changeGear(expected_gear)

        where:
        current_gear | expected_gear
        firstGear    | firstGear
    }

    @Unroll
    def "should downshift in MANUAL from #current_gear to #expected_gear, engine #engine_rpm"() {

        given: "vehicle is in manual"
        gearbox.currentGear() >> current_gear
        changeToManual(gearboxDriver)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "paddle is used gearbox is changing a gear"
        gearboxDriver.onPaddleUse(downshift)

        then:
        1 * gearbox.changeGear(expected_gear)

        where:
        current_gear | engine_rpm | expected_gear
        secondGear   | veryLowRpm | firstGear
        fourthGear   | veryLowRpm | thirdGear
        secondGear   | lowRpm     | firstGear
        fourthGear   | lowRpm     | thirdGear
        secondGear   | mediumRpm  | firstGear
        fourthGear   | mediumRpm  | thirdGear
        secondGear   | highRpm    | firstGear
        fourthGear   | highRpm    | thirdGear
    }

    @Unroll
    def "should upshift in MANUAL from #current_gear to #expected_gear, engine #engine_rpm"() {

        given: "vehicle is in manual"
        gearbox.currentGear() >> current_gear
        changeToManual(gearboxDriver)
        gearboxDriver.onEngineRevsChange(engine_rpm)

        when: "paddle is used gearbox is changing a gear"
        gearboxDriver.onPaddleUse(upshift)

        then:
        1 * gearbox.changeGear(expected_gear)

        where:
        current_gear | engine_rpm  | expected_gear
        secondGear   | lowRpm      | thirdGear
        firstGear    | lowRpm      | secondGear
        secondGear   | mediumRpm   | thirdGear
        firstGear    | mediumRpm   | secondGear
        secondGear   | highRpm     | thirdGear
        firstGear    | highRpm     | secondGear
        secondGear   | veryHighRpm | thirdGear
        firstGear    | veryHighRpm | secondGear
    }
}
