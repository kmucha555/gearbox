package pl.mkjb.gearbox.settings;

import pl.mkjb.gearbox.external.shared.Event;

public enum GearboxState implements Event {
    PARK, REVERSE, NEUTRAL, DRIVE, COAST
}
