package pl.mkjb.gearbox.external


import pl.mkjb.gearbox.external.shared.Listener
import pl.mkjb.gearbox.external.shared.ThrottleThreshold
import spock.lang.Shared
import spock.lang.Specification

class ThrottleSpec extends Specification {
    @Shared
    def throttle = new Throttle();

    def "should throw exception when registering null as car component"() {
        when:
        throttle.register(null)

        then:
        thrown(IllegalArgumentException)
    }

    def "should throw exception when unregistering null as car component"() {
        when:
        throttle.unregister(null)

        then:
        thrown(IllegalArgumentException)
    }

    def "should send event to subscribers when new throttle threshold is given"() {
        given:
        def threshold = new ThrottleThreshold(50)
        def carComponent = Mock(Listener)
        throttle.register(carComponent)

        when:
        throttle.sendEvent(threshold)

        then:
        1 * carComponent.onThrottleChange(threshold)
    }
}
