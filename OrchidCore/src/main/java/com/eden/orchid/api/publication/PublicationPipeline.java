package com.eden.orchid.api.publication;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.components.ModularList;

import javax.inject.Inject;

public final class PublicationPipeline extends ModularList<PublicationPipeline, OrchidPublisher> {

    @Inject
    public PublicationPipeline(OrchidContext context) {
        super(context);
    }

    @Override
    protected Class<OrchidPublisher> getItemClass() {
        return OrchidPublisher.class;
    }

}
