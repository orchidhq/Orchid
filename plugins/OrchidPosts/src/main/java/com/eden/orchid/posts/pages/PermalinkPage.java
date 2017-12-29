package com.eden.orchid.posts.pages;

import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;


public abstract class PermalinkPage extends OrchidPage {

    public PermalinkPage(OrchidResource resource, String key) {
        super(resource, key);
    }

    public PermalinkPage(OrchidResource resource, String key, String title) {
        super(resource, key, title);
    }

    public PermalinkPage(OrchidResource resource, String key, String title, String path) {
        super(resource, key, title, path);
    }

    public abstract String getPermalink();

}
