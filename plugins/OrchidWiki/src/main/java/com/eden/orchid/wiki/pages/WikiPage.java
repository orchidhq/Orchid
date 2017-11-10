package com.eden.orchid.wiki.pages;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.resources.resource.OrchidResource;
import lombok.Getter;
import lombok.Setter;

public class WikiPage extends OrchidPage {

    @Getter @Setter private int order;
    @Getter @Setter private WikiSummaryPage sectionSummary;

    public WikiPage(OrchidResource resource, String title) {
        super(resource, "wiki", title);
    }

}
