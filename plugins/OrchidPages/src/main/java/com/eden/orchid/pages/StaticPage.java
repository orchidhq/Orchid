package com.eden.orchid.pages;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.resources.resource.OrchidResource;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StaticPage extends OrchidPage {
    public StaticPage(OrchidResource resource) {
        super(resource, "page", String.join(" ",
                Arrays.stream(resource.getReference().getTitle().split("_")).map(s ->
                        s.substring(0, 1).toUpperCase() + s.substring(1)
                ).collect(Collectors.toList())
        ));
    }
}
