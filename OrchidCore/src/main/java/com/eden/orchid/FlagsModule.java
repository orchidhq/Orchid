package com.eden.orchid;

import com.eden.orchid.api.options.OrchidFlags;
import com.eden.orchid.api.registration.IgnoreModule;
import com.google.inject.AbstractModule;

import java.util.Map;

@IgnoreModule
public class FlagsModule extends AbstractModule {

    private Map<String, String[]> flags;

    public FlagsModule(Map<String, String[]> flags) {
        this.flags = flags;
    }

    @Override
    protected void configure() {
        install(OrchidFlags.getInstance().parseFlags(this.flags));
    }

}
