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
        gearbox.currentGear() >> thirdGear
        changeToDrive(gearboxDriver)
        gearboxDriver.onDriveModeChange(drive_mode)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when: "paddle is used"
        gearboxDriver.onPaddleUse(upshift)

        then: "gearbox state is switched to manual"
        gearboxDriver.checkGearboxState() == output

        where:
        drive_mode | output
        ECO        | MANUAL
        COMFORT    | MANUAL
        SPORT      | MANUAL
    }

    def "should change gearbox state to MANUAL when paddle is used no matter which aggressive mode is used (#aggressive_mode)"() {

        given: "gearbox in drive mode on third gear"
        gearbox.currentGear() >> thirdGear
        changeToDrive(gearboxDriver)
        gearboxDriver.onGearChangeMode(aggressive_mode)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when: "paddle is used"
        gearboxDriver.onPaddleUse(upshift)

        then: "gearbox state is switched to manual"
        gearboxDriver.checkGearboxState() == output

        where:
        aggressive_mode | output
        SOFT            | MANUAL
        HARD            | MANUAL
        EXTREME         | MANUAL
    }

    @Unroll
    def "should upshift from #input to #output using paddles"() {

        given:
        gearbox.currentGear() >> input
        changeToDrive(gearboxDriver)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when:
        gearboxDriver.onPaddleUse(upshift)

        then:
        1 * gearbox.changeGear(output)

        where:
        input     | output
        firstGear | secondGear
        thirdGear | fourthGear
    }

    @Unroll
    def "should not upshift from #input to #output using paddles when engine revs are too low"() {

        given:
        gearbox.currentGear() >> input
        changeToDrive(gearboxDriver)
        gearboxDriver.onEngineRevsChange(veryLowRpm)

        when:
        gearboxDriver.onPaddleUse(upshift)

        then:
        1 * gearbox.changeGear(output)

        where:
        input     | output
        firstGear | firstGear
        thirdGear | thirdGear
    }

    @Unroll
    def "should not upshift when gearbox is on max #input.gear th gear using paddles"() {

        given:
        gearbox.currentGear() >> input
        changeToDrive(gearboxDriver)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when:
        gearboxDriver.onPaddleUse(upshift)

        then:
        1 * gearbox.changeGear(output)

        where:
        input   | output
        maxGear | maxGear
    }

    @Unroll
    def "should downshift one gear from #input to #output using paddles"() {

        given:
        gearbox.currentGear() >> input
        changeToDrive(gearboxDriver)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when:
        gearboxDriver.onPaddleUse(downshift)

        then:
        1 * gearbox.changeGear(output)

        where:
        input      | output
        secondGear | firstGear
        fourthGear | thirdGear
    }

    @Unroll
    def "should not downshift from #input to #output using paddles when engine revs are too high"() {

        given:
        gearbox.currentGear() >> input
        changeToDrive(gearboxDriver)
        gearboxDriver.onEngineRevsChange(veryHighRpm)

        when:
        gearboxDriver.onPaddleUse(downshift)

        then:
        1 * gearbox.changeGear(output)

        where:
        input      | output
        fourthGear | fourthGear
        secondGear | secondGear
    }

    def "should not downshift when gearbox is on first gear using paddles"() {

        given:
        gearbox.currentGear() >> input
        changeToDrive(gearboxDriver)
        gearboxDriver.onEngineRevsChange(mediumRpm)

        when:
        gearboxDriver.onPaddleUse(downshift)

        then:
        1 * gearbox.changeGear(output)

        where:
        input     | output
        firstGear | firstGear
    }
}
