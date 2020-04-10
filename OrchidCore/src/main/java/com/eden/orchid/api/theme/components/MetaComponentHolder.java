package com.eden.orchid.api.theme.components;

import com.eden.orchid.api.theme.pages.OrchidPage;
import kotlin.collections.CollectionsKt;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

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

    private synchronized List<OrchidComponent> get(@Nonnull OrchidPage page, OrchidComponent.MetaLocation metaLocation) {
        return CollectionsKt.filter(super.get(page), it -> it.getMetaLocation() == metaLocation);
    }

    public synchronized List<OrchidComponent> getHeadComponents(@Nonnull OrchidPage page) {
        return get(page, OrchidComponent.MetaLocation.head);
    }

    public synchronized List<OrchidComponent> getBodyStartComponents(@Nonnull OrchidPage page) {
        return get(page, OrchidComponent.MetaLocation.bodyStart);
    }

    public synchronized List<OrchidComponent> getBodyEndComponents(@Nonnull OrchidPage page) {
        return get(page, OrchidComponent.MetaLocation.bodyEnd);
    }
}
