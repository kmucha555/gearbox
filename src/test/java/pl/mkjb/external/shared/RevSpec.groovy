package pl.mkjb.external.shared

import spock.lang.Specification
import spock.lang.Unroll

import static pl.mkjb.Settings.*

class RevSpec extends Specification {

    @Unroll
    def "should return given rpm value"() {
        expect:
        new Rev(input).actualRevs == output

        where:
        input    | output
        ZERO_RPM | ZERO_RPM
        IDLE_RPM | IDLE_RPM
        MAX_RPM  | MAX_RPM
    }

    @Unroll
    def "should throw exception when rpm value is negative"() {
        when:
        new Rev(input)

        then:
        thrown(IllegalArgumentException)

        where:
        input | output
        -1    | _
        -100  | _
    }
}
