package com.eden.orchid.wiki.pages;

import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;

public class WikiSummaryPage extends OrchidPage {
    public WikiSummaryPage(OrchidResource resource, String title) {
        super(resource, "wikiSummary", title);
    }
}
