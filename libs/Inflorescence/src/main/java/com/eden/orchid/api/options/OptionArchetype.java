package com.eden.orchid.api.options;

import org.json.JSONObject;

public interface OptionArchetype {

    JSONObject getOptions(Object target, String archetypeKey);

}
