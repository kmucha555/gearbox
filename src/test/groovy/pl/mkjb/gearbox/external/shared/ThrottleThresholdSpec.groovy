package pl.mkjb.gearbox.external.shared


import spock.lang.Specification
import spock.lang.Unroll

import static pl.mkjb.gearbox.settings.Setting.*

class ThrottleThresholdSpec extends Specification {

    @Unroll
    def "should return given threshold level"() {
        expect:
        new ThrottleThreshold(input).level == output

        where:
        input         | output
        ZERO_THRESHOLD | ZERO_THRESHOLD
        HALF_THRESHOLD | HALF_THRESHOLD
        MAX_THRESHOLD | MAX_THRESHOLD
    }

    @Unroll
    def "should throw exception when threshold is out of bounds"() {
        when:
        new ThrottleThreshold(input)

        then:
        thrown(IllegalArgumentException)

        where:
        input | output
        -1    | _
        101   | _
    }
}
