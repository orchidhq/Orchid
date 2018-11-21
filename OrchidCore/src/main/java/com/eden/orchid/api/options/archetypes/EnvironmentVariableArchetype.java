package com.eden.orchid.api.options.archetypes;

import com.eden.orchid.api.options.OptionArchetype;
import com.eden.orchid.api.options.annotations.Description;

import java.util.Map;

@Description(value = "Loads additional values from secure environment variables.",
        name = "Environment Variables"
)
public class EnvironmentVariableArchetype implements OptionArchetype {

    @Override
    public Map<String, Object> getOptions(Object target, String archetypeKey) {
        Map<String, ?> vars = System.getenv();
        return (Map<String, Object>) vars;
    }

}
