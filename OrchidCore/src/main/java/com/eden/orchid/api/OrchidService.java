package com.eden.orchid.api;

public interface OrchidService {

    void initialize(OrchidContext context);

    default void onStart() { }

    default void onFinish() { }

    @SuppressWarnings("unchecked")
    default <T extends OrchidService> T getService(Class<T> serviceClass) {
        return (T) this;
    }

}
