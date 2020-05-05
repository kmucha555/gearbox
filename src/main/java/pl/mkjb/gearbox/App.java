package pl.mkjb.gearbox;

import com.google.common.eventbus.EventBus;
import lombok.val;
import pl.mkjb.gearbox.external.ExternalFacade;
import pl.mkjb.gearbox.gearbox.GearboxFacade;

public class App {
    public static void main(String[] args) {

        val gearboxDriver = GearboxFacade.run();
        val eventBus = new EventBus();
        val externalFacade = ExternalFacade.connectExternalSystem(eventBus);

        eventBus.register(gearboxDriver);

        externalFacade.start();
    }
}
