package pl.mkjb.external.shared


import spock.lang.Specification
import spock.lang.Unroll

class RpmTestSpec extends Specification {

    @Unroll
    def "should return given rpm value"() {
        expect:
        new Rpm(input).rpm == output

        where:
        input | output
        0     | 0
        750   | 750
        5000  | 5000
    }

    @Unroll
    def "should throw exception when rpm value is negative"() {
        when:
        new Rpm(input)

        then:
        thrown(IllegalArgumentException)

        where:
        input | output
        -1    | _
        -100  | _
    }
}
