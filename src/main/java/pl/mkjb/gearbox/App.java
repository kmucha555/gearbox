package pl.mkjb.gearbox;

import com.google.common.eventbus.EventBus;
import lombok.val;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import pl.mkjb.gearbox.external.ExternalFacade;
import pl.mkjb.gearbox.gearbox.GearboxDriver;

public class App {
    public static void main(String[] args) {
        Configurator.setLevel("pl.mkjb.gearbox", Level.INFO);

        val gearboxDriver = GearboxDriver.powerUpGearbox();

        val eventBus = new EventBus();
        val externalFacade = ExternalFacade.connectExternalSystem(eventBus);

        eventBus.register(gearboxDriver);

        externalFacade.start();
    }
}
