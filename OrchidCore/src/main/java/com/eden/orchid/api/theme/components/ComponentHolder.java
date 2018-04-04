package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.OrchidContext;
import org.json.JSONArray;

import javax.inject.Inject;

public final class ComponentHolder extends ModularList<ComponentHolder, OrchidComponent> {

    @Inject
    public ComponentHolder(OrchidContext context, JSONArray componentsJson) {
        super(context, OrchidComponent.class, componentsJson);
    }

}
