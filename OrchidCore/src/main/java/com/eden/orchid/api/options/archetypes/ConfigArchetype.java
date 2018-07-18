package com.eden.orchid.api.options.archetypes;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionArchetype;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Map;

public class ConfigArchetype implements OptionArchetype {

    private final OrchidContext context;

    @Inject
    public ConfigArchetype(OrchidContext context) {
        this.context = context;
    }

    @Override
    public Map<String, Object> getOptions(Object target, String archetypeKey) {
        Map<String, Object> configOptions = null;
        Map<String, Object> eventOptions = null;

        if(!EdenUtils.isEmpty(archetypeKey)) {
            JSONElement contextOptions = context.query(archetypeKey);
            if(EdenUtils.elementIsObject(contextOptions)) {
                configOptions = ((JSONObject) contextOptions.getElement()).toMap();
            }
        }

        Orchid.Lifecycle.ArchetypeOptions options = new Orchid.Lifecycle.ArchetypeOptions(archetypeKey, target);
        context.broadcast(options);
        eventOptions = options.getConfig();

        return EdenUtils.merge(configOptions, eventOptions);
    }

}
