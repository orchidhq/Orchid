package com.eden.orchid.api.options.archetypes;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionArchetype;
import org.json.JSONObject;

import javax.inject.Inject;

public class EventArchetype implements OptionArchetype {

    private final OrchidContext context;

    @Inject
    public EventArchetype(OrchidContext context) {
        this.context = context;
    }

    @Override
    public JSONObject getOptions(Object target, String archetypeKey) {
        Orchid.Lifecycle.ArchetypeOptions options = new Orchid.Lifecycle.ArchetypeOptions(archetypeKey, target);
        context.broadcast(options);
        return options.getConfig();
    }

}
