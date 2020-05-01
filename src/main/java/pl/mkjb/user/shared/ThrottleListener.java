package pl.mkjb.user.shared;

public interface ThrottleListener {
    void onThrottleChange(ThrottleThreshold throttleThreshold);
}