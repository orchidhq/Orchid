package com.eden.orchid.api.options;

import org.json.JSONObject;

public interface OptionArchetype extends OptionsHolder {

    JSONObject getOptions(Object target, String archetypeKey);

}
