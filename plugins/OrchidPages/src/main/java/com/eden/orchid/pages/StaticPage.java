package com.eden.orchid.pages;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.resources.resource.OrchidResource;

public class StaticPage extends OrchidPage {
    public StaticPage(OrchidResource resource) {
        super(resource);
        this.type = "page";
    }
}
