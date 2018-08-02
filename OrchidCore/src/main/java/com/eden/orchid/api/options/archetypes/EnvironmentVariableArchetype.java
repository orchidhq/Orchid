package com.eden.orchid.api.options.archetypes;

import com.eden.orchid.api.options.OptionArchetype;

import java.util.Map;

public class EnvironmentVariableArchetype implements OptionArchetype {

    @Override
    public Map<String, Object> getOptions(Object target, String archetypeKey) {
        Map<String, ?> vars = System.getenv();
        return (Map<String, Object>) vars;
    }

}
