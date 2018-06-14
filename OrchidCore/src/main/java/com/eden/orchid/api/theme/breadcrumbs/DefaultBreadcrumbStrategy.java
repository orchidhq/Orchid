package com.eden.orchid.api.theme.breadcrumbs;

import com.eden.orchid.api.theme.pages.OrchidPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DefaultBreadcrumbStrategy implements BreadcrumbStrategy {

    @Override
    public List<Breadcrumb> getBreadcrumbs(OrchidPage page) {
        List<Breadcrumb> breadcrumbs = new ArrayList<>();

        while(page != null) {
            breadcrumbs.add(new Breadcrumb(page));
            page = page.getParent();
        }

        Collections.reverse(breadcrumbs);

        return breadcrumbs;
    }

}
