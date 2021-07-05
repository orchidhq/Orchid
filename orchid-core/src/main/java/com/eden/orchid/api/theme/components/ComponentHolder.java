package com.eden.orchid.api.theme.components;

import javax.inject.Inject;

public class ComponentHolder extends ModularPageList<ComponentHolder, OrchidComponent> {

    @Inject
    public ComponentHolder() {
        super();
        setDefaultType("template");
    }

    @Override
    protected Class<OrchidComponent> getItemClass() {
        return OrchidComponent.class;
    }

}
