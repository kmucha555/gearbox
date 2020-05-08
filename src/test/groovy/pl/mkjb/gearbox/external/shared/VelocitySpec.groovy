package pl.mkjb.gearbox.external.shared

import spock.lang.Specification
import spock.lang.Unroll

import static pl.mkjb.gearbox.settings.Setting.*

class VelocitySpec extends Specification {
    @Unroll
    def "should return given rpm value"() {
        expect:
        new LinearSpeed(input).actualSpeed == output

        where:
        input            | output
        NO_SPEED         | NO_SPEED
        MIN_LINEAR_SPEED | MIN_LINEAR_SPEED
        MAX_LINEAR_SPEED | MAX_LINEAR_SPEED
    }

    @Unroll
    def "should throw exception when velocity is out of bounds"() {
        when:
        new LinearSpeed(input)

        then:
        thrown(IllegalArgumentException)

        where:
        input | output
        -100  | _
        300   | _
    }
}
