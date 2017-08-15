package com.eden.orchid.impl.themes.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;

public class LicenseComponent extends OrchidComponent {

    @Inject
    public LicenseComponent(OrchidContext context) {
        super(40, "license", context);
    }

    public String getContent() {
        OrchidResource readmeResource = context.findClosestFile("license");
        if (readmeResource != null) {
            return context.compile(readmeResource.getReference().getExtension(), readmeResource.getContent(), this);
        }
        return null;
    }
}
