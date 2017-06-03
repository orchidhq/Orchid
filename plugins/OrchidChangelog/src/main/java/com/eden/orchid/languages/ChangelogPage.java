package com.eden.orchid.languages;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.resources.resource.OrchidResource;

public class ChangelogPage extends OrchidPage {
    public ChangelogPage(OrchidResource resource) {
        super(resource);
        this.type = "changelog";
    }
}
