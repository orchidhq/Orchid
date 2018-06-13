package com.eden.orchid.api.theme.breadcrumbs;

import java.util.List;

public interface BreadcrumbHolder {

    BreadcrumbHolder getBreadcrumbHolder();

    default Breadcrumbs getBreadCrumbs() {
        return getBreadcrumbHolder().getBreadCrumbs();
    }

    default Breadcrumbs getBreadCrumbs(String key) {
        return getBreadcrumbHolder().getBreadCrumbs(key);
    }

    default List<Breadcrumbs> getAllBreadCrumbs()  {
        return getBreadcrumbHolder().getAllBreadCrumbs();
    }

    default void addBreadcrumbs(Breadcrumbs breadcrumbs) {
        getBreadcrumbHolder().addBreadcrumbs(breadcrumbs);
    }

    default void setDefaultBreadcrumbs(String key) {
        getBreadcrumbHolder().setDefaultBreadcrumbs(key);
    }

}
