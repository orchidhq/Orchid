package com.eden.orchid.api.compilers;

import com.eden.orchid.api.registration.Prioritized;
import org.json.JSONObject;

public abstract class OrchidParser extends Prioritized {

    public static final String arrayAsObjectKey = "listData";

    public abstract String[] getSourceExtensions();

    public abstract JSONObject parse(String extension, String input);

}
