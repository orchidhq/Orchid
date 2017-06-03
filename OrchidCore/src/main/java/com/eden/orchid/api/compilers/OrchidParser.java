package com.eden.orchid.api.compilers;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.registration.Prioritized;

public abstract class OrchidParser extends Prioritized {

    public abstract String[] getSourceExtensions();

    public abstract JSONElement parse(String extension, String input);

}
