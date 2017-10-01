package com.eden.orchid.api;

import com.eden.orchid.api.options.OptionsHolder;

public interface OrchidService extends OptionsHolder {

    default String getKey() { return getClass().getSimpleName().replaceAll("Impl", ""); }

    void initialize(OrchidContext context);

    default void onStart() { }

    default void onPostStart() { }

    default void onFinish() { }

    @SuppressWarnings("unchecked")
    default <T extends OrchidService> T getService(Class<T> serviceClass) {
        return (T) this;
    }

}
