package com.eden.orchid.api.options.archetypes;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionArchetype;
import org.json.JSONObject;

import javax.inject.Inject;

public class ConfigArchetype implements OptionArchetype {

    private final OrchidContext context;

    @Inject
    public ConfigArchetype(OrchidContext context) {
        this.context = context;
    }

    @Override
    public JSONObject getOptions(Object target, String archetypeKey) {
        if(!EdenUtils.isEmpty(archetypeKey)) {
            JSONElement contextOptions = context.query(archetypeKey);
            if(contextOptions != null && contextOptions.getElement() instanceof JSONObject) {
                return (JSONObject) contextOptions.getElement();
            }
        }

        return null;
    }

}
