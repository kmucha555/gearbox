package pl.mkjb.external;

import lombok.RequiredArgsConstructor;
import pl.mkjb.external.shared.Rpm;

@RequiredArgsConstructor
public class ExternalFacade {
    private final EngineRpm engineRPM;

    public Rpm getEngineRPM() {
        return new Rpm(engineRPM.getEngineRpm());
    }
}
