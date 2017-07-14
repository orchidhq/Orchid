package com.eden.orchid.api.theme.pages;

import com.eden.orchid.api.resources.resource.ExternalResource;

/**
 * An OrchidExternalPage is little more than an OrchidReference which points to a page on an external site. Much of the
 * functionality for Pages is disabled for this page type, and is intended just to be used so external pages can be
 * linked to in the same way as internal pages.
 */
public class OrchidExternalPage extends OrchidPage {

    public OrchidExternalPage(OrchidReference reference) {
        super(new ExternalResource(reference), "ext");
    }

    @Override
    public String getContent() {
        throw new UnsupportedOperationException("This method is not allowed on OrchidExternalPage");
    }
}
