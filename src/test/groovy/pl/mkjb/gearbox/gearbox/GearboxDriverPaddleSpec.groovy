package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.PreparedInput
import spock.lang.Specification

class GearboxDriverPaddleSpec extends Specification implements PreparedInput {
    Gearbox gearbox = Mock()
    GearboxDriver gearboxDriver

    def setup() {
        gearboxDriver = GearboxDriver.powerUpGearbox(gearbox)
    }

    def "should upshift one gear using paddles"() {
        given:
        gearbox.currentGear() >> input
        changeToDrive(gearboxDriver)

        when:
        gearboxDriver.onPaddleUse(upshift)

        then:
        1 * gearbox.changeGear(output)

        where:
        input     | output
        firstGear | secondGear
        thirdGear | fourthGear
    }

    def "should not upshift when gearbox is on max gear using paddles"() {
        given:
        gearbox.currentGear() >> input
        changeToDrive(gearboxDriver)

        when:
        gearboxDriver.onPaddleUse(upshift)

        then:
        0 * gearbox.changeGear(output)

        where:
        input     | output
        maxGear | maxGear
    }

    def "should downshift one gear using paddles"() {
        given:
        gearbox.currentGear() >> input
        changeToDrive(gearboxDriver)

        when:
        gearboxDriver.onPaddleUse(downshift)

        then:
        1 * gearbox.changeGear(output)

        where:
        input     | output
        secondGear | firstGear
        fourthGear | thirdGear
    }

    def "should not downshift when gearbox is on first gear using paddles"() {
        given:
        gearbox.currentGear() >> input
        changeToDrive(gearboxDriver)

        when:
        gearboxDriver.onPaddleUse(downshift)

        then:
        0 * gearbox.changeGear(output)

        where:
        input     | output
        firstGear | firstGear
    }
}
