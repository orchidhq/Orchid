package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.OrchidContext;

import javax.inject.Inject;

public final class MetaComponentHolder extends ComponentHolder {

    @Inject
    public MetaComponentHolder(OrchidContext context) {
        super(context);
        setDefaultType("template");
    }

    @Override
    protected Class<OrchidComponent> getItemClass() {
        return OrchidComponent.class;
    }

    @Override
    protected boolean isTypeEligible(OrchidComponent item) {
        return item.meta;
    }

}
