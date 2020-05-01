package pl.mkjb.user.shared

import spock.lang.Specification
import spock.lang.Unroll

class BrakeThresholdSpec extends Specification {

    @Unroll
    def "should return given threshold level"() {
        expect:
        new BrakeThreshold(input).level == output

        where:
        input | output
        0     | 0
        55    | 55
        100   | 100
    }

    @Unroll
    def "should throw exception when threshold is out of bounds"() {
        when:
        new BrakeThreshold(input)

        then:
        thrown(IllegalArgumentException)

        where:
        input | output
        -1    | _
        101   | _
    }
}
