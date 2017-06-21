package com.eden.orchid.wiki;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.resources.resource.OrchidResource;

public class WikiPage extends OrchidPage {

    private int order;

    public WikiPage(OrchidResource resource) {
        super(resource);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
