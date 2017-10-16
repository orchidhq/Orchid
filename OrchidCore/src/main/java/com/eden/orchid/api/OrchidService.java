package com.eden.orchid.api;

import com.eden.orchid.api.options.OptionsHolder;

public interface OrchidService extends OptionsHolder {

    default String getKey() {
        String key = getClass().getSimpleName();
        key = key.replaceAll("Service", "");
        key = key.replaceAll("Impl", "");
        key = key.substring(0, 1).toLowerCase() + key.substring(1);
        if(!key.endsWith("s")) {
            key = key + "s";
        }
        return key;
    }

    void initialize(OrchidContext context);

    default void onStart() { }

    default void onPostStart() { }

    default void onFinish() { }

    @SuppressWarnings("unchecked")
    default <T extends OrchidService> T getService(Class<T> serviceClass) {
        return (T) this;
    }

}
