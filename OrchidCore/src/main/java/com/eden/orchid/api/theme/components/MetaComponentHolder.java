package com.eden.orchid.api.theme.components;

import javax.inject.Inject;

public final class MetaComponentHolder extends ComponentHolder {

    @Inject
    public MetaComponentHolder() {
        super();
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
