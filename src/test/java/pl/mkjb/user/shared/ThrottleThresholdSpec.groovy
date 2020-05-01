package pl.mkjb.user.shared

import pl.mkjb.user.shared.ThrottleThreshold
import spock.lang.Specification
import spock.lang.Unroll

import static pl.mkjb.Settings.*

class ThrottleThresholdSpec extends Specification {

    @Unroll
    def "should return given threshold level"() {
        expect:
        new ThrottleThreshold(input).level == output

        where:
        input | output
        MIN_THRESHOLD  | MIN_THRESHOLD
        HALF_THRESHOLD | HALF_THRESHOLD
        MAX_THRESHOLD  | MAX_THRESHOLD
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
