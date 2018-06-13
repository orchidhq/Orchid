package com.eden.orchid.api.theme.breadcrumbs;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(DefaultBreadcrumbStrategy.class)
public interface BreadcrumbStrategy {

    List<Breadcrumb> getBreadcrumbs(OrchidPage page);

}
