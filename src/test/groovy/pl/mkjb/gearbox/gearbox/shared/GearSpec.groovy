package pl.mkjb.gearbox.gearbox.shared

import spock.lang.Specification
import spock.lang.Unroll

import static pl.mkjb.gearbox.settings.Setting.MAX_GEAR_NUMBER
import static pl.mkjb.gearbox.settings.Setting.MIN_GEAR_NUMBER

class GearSpec extends Specification {
    @Unroll
    def "should return given gear"() {
        expect:
        new Gear(input).gear == output

        where:
        input           | output
        MIN_GEAR_NUMBER | MIN_GEAR_NUMBER
        MAX_GEAR_NUMBER | MAX_GEAR_NUMBER
    }

    @Unroll
    def "should throw exception when gear is out of bounds"() {
        when:
        new Gear(input)

        then:
        thrown(IllegalArgumentException)

        where:
        input | output
        -2    | _
        15    | _
    }
}
