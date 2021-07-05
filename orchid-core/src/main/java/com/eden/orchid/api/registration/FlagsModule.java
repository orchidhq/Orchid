package com.eden.orchid.api.registration;

import com.eden.orchid.api.options.OrchidFlags;
import com.eden.orchid.utilities.OrchidUtils;

import java.util.HashMap;
import java.util.Map;

@IgnoreModule
public class FlagsModule extends OrchidModule {

    private Map<String, Object> flagsMap;

    public FlagsModule(String[] args, Map<String, Object> flagsMap) {
        this.flagsMap = new HashMap<>();
        if(flagsMap != null) {
            this.flagsMap.putAll(flagsMap);
        }
        if(args != null) {
            this.flagsMap.putAll(OrchidUtils.parseCommandLineArgs(args));
        }
    }

    @Override
    protected void configure() {
        install(OrchidFlags.getInstance().parseFlags(flagsMap));
    }

}
