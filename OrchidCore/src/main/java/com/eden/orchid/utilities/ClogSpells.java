package com.eden.orchid.utilities;

import com.caseyjbrooks.clog.parseltongue.Spell;
import com.eden.orchid.Orchid;

public class ClogSpells {

    @Spell
    public static String baseUrl(Object object) {
        return Orchid.getInstance().getContext().getSite().getBaseUrl();
    }

    @Spell
    public static String orchidVersion(Object object) {
        return Orchid.getInstance().getContext().getSite().getOrchidVersion();
    }

    @Spell
    public static String env(Object object) {
        return Orchid.getInstance().getContext().getSite().getEnvironment();
    }

    @Spell
    public static String version(Object object) {
        return Orchid.getInstance().getContext().getSite().getVersion();
    }

}
