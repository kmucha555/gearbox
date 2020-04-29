package pl.mkjb.gearbox.gearbox

import pl.mkjb.gearbox.gearbox.shared.GearLimit
import spock.lang.Specification
import spock.lang.Unroll

class MaxGearNumberSpec extends Specification {

    @Unroll
    def "should return given max number of gears"() {
        expect: "returns max number of gears"
        new GearLimit(input).getMaxGearNumber() == output

        where:
        input | output
        6     | 6
        8     | 8
        10    | 10
    }

    @Unroll
    def "should throw exception when gear number is outside bounds"() {
        when:
        new GearLimit(input)

        then:
        thrown(IllegalArgumentException)

        where:
        input | output
        -3    | _
        0     | _
        5     | _
        11    | _
    }
}
