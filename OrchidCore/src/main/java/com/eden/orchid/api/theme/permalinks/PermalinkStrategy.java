package com.eden.orchid.api.theme.permalinks;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultPermalinkStrategy.class)
public interface PermalinkStrategy {

    void applyPermalink(OrchidPage page, String permalink);

    void applyPermalink(OrchidPage page, String permalink, boolean prettyUrl);

}
