package com.eden.orchid.api.theme.breadcrumbs;

import com.eden.orchid.api.theme.pages.OrchidPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public final class DefaultBreadcrumbStrategy implements BreadcrumbStrategy {

    @Override
    public List<Breadcrumb> getBreadcrumbs(final OrchidPage page) {
        LinkedHashMap<OrchidPage, OrchidPage> breadcrumbPagesMap = new LinkedHashMap<>();

        OrchidPage parentPage = page;
        while(parentPage != null) {
            if(breadcrumbPagesMap.containsKey(parentPage)) break;

            breadcrumbPagesMap.put(parentPage, parentPage);
            parentPage = parentPage.getParent();
        }

        List<OrchidPage> breadcrumbPages = new ArrayList<>(breadcrumbPagesMap.values());
        Collections.reverse(breadcrumbPages);
        return breadcrumbPages
                .stream()
                .map(Breadcrumb::new)
                .collect(Collectors.toList())
                ;
    }

}
