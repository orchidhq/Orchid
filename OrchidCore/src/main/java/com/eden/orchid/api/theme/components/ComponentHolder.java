package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.OrchidContext;

import javax.inject.Inject;

public class ComponentHolder extends ModularPageList<ComponentHolder, OrchidComponent> {

    @Inject
    public ComponentHolder(OrchidContext context) {
        super(context);
        setDefaultType("template");
    }

    @Override
    protected Class<OrchidComponent> getItemClass() {
        return OrchidComponent.class;
    }

}
