package com.eden.orchid.api;

import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.utilities.SuppressedWarnings;

@Description(value = "The core, common API for working with Orchid.", name = "Services")
public interface OrchidService extends OptionsHolder {

    void initialize(OrchidContext context);

    default void onStart() { }

    default void onPostStart() { }

    default void onFinish() { }

    @SuppressWarnings(SuppressedWarnings.UNCHECKED_JAVA)
    default <T extends OrchidService> T getService(Class<T> serviceClass) {
        return (T) this;
    }

}
