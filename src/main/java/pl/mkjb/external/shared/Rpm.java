package pl.mkjb.external.shared;

public class Rpm {
    public final int rpm;

    public Rpm(int rpm) {
        if (rpm < 0) {
            throw new IllegalArgumentException("RPM must be positive");
        }

        this.rpm = rpm;
    }
}
