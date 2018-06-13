package com.eden.orchid.api.theme.breadcrumbs;


import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;

import java.util.List;

public final class Breadcrumbs extends Prioritized {

    @Getter
    private final String key;
    private final BreadcrumbStrategy breadcrumbStrategy;

    private boolean loaded;
    private List<Breadcrumb> breadcrumbList;

    public Breadcrumbs(String key, BreadcrumbStrategy breadcrumbStrategy, int priority) {
        super(priority);
        this.key = key;
        this.breadcrumbStrategy = breadcrumbStrategy;

        loaded = false;
        breadcrumbList = null;
    }

    public final List<Breadcrumb> get(OrchidPage page) {
        if(breadcrumbList == null && !loaded) {
            breadcrumbList = breadcrumbStrategy.getBreadcrumbs(page);
            loaded = true;
        }
        return breadcrumbList;
    }

}
