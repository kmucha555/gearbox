package pl.mkjb.user

import pl.mkjb.user.shared.BrakeListener
import pl.mkjb.user.shared.BrakeThreshold
import spock.lang.Shared
import spock.lang.Specification

class BrakeTestSpec extends Specification {
    @Shared
    def brake = new Brake();

    def "should throw exception when registering null as car component"() {
        when:
        brake.register(null)

        then:
        thrown(IllegalArgumentException)
    }

    def "should throw exception when unregistering null as car component"() {
        when:
        brake.unregister(null)

        then:
        thrown(IllegalArgumentException)
    }

    def "should send event to subscribers when new brake threshold is given"() {
        given:
        def threshold = new BrakeThreshold(50)
        def carComponent = Mock(BrakeListener)
        brake.register(carComponent)

        when:
        brake.sendEvent(threshold)

        then:
        1 * carComponent.onBrakeApplied(threshold)
    }
}
