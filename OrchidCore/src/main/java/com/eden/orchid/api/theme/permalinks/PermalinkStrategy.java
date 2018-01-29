package com.eden.orchid.api.theme.permalinks;

import com.eden.orchid.api.theme.pages.OrchidPage;

public interface PermalinkStrategy {

    void applyPermalink(OrchidPage page, String permalink);

}
