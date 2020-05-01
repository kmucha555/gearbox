package pl.mkjb.gearbox.gearbox;

import com.google.common.eventbus.Subscribe;
import pl.mkjb.gearbox.external.shared.LinearSpeed;
import pl.mkjb.gearbox.external.shared.RevGauge;

class ExternalSystem {
    private RevGauge revGauge;
    private LinearSpeed linearSpeed;

    @Subscribe
    public void onEngineRevsChange(RevGauge revGauge) {
        this.revGauge = revGauge;
    }

    @Subscribe
    public void onVelocityChange(LinearSpeed linearSpeed) {
        this.linearSpeed = linearSpeed;
    }
}
