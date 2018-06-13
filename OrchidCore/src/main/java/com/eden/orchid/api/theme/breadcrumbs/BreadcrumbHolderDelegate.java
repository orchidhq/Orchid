package com.eden.orchid.api.theme.breadcrumbs;


import com.eden.orchid.api.OrchidContext;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BreadcrumbHolderDelegate implements BreadcrumbHolder {

    private final OrchidContext context;
    private final Map<String, Breadcrumbs> breadcrumbs;

    @Setter
    private String defaultBreadcrumbs;

    @Inject
    public BreadcrumbHolderDelegate(OrchidContext context) {
        this.context = context;
        this.breadcrumbs = new HashMap<>();
    }

    @Override
    public BreadcrumbHolder getBreadcrumbHolder() {
        return this;
    }

    @Override
    public Breadcrumbs getBreadCrumbs() {
        if(breadcrumbs.get(defaultBreadcrumbs) == null) {
            Breadcrumbs highestPriority = breadcrumbs
                    .values()
                    .stream()
                    .max(Comparator.comparing(Breadcrumbs::getPriority))
                    .orElse(null);

            if(highestPriority != null) {
                return highestPriority;
            }
            else {
                return breadcrumbs.computeIfAbsent(defaultBreadcrumbs, s -> new Breadcrumbs(defaultBreadcrumbs, context.resolve(BreadcrumbStrategy.class), 0));
            }
        }
        else {
            return breadcrumbs.get(defaultBreadcrumbs);
        }
    }

    @Override
    public Breadcrumbs getBreadCrumbs(String key) {
        return breadcrumbs.get(key);
    }

    @Override
    @NotNull
    public List<Breadcrumbs> getAllBreadCrumbs() {
        return new ArrayList<>(breadcrumbs.values());
    }

    @Override
    public void addBreadcrumbs(@NotNull Breadcrumbs breadcrumbs) {
        this.breadcrumbs.put(breadcrumbs.getKey(), breadcrumbs);
    }

}
