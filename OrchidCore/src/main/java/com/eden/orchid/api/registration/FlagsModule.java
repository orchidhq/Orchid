package com.eden.orchid.api.registration;

import com.eden.orchid.api.options.OrchidFlags;
import com.google.inject.AbstractModule;

@IgnoreModule
public class FlagsModule extends AbstractModule {

    private String[] flags;

    public FlagsModule(String[] flags) {
        this.flags = flags;
    }

    @Override
    protected void configure() {
        install(OrchidFlags.getInstance().parseFlags(this.flags));
    }

}
