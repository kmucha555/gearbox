package pl.mkjb.gearbox.external;

public interface Component<T> {
    void sendEvent(T event);
}
