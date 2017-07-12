package com.eden.orchid.impl.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;

public class ReadmeComponent extends OrchidComponent {

    @Inject
    public ReadmeComponent(OrchidContext context, OrchidResources resources) {
        super(context, resources);
        this.alias = "readme";
    }

    @Override
    public String getContent() {
        OrchidResource readmeResource = resources.findClosestFile("readme");
        if (readmeResource != null) {
            return renderString(readmeResource.getReference().getExtension(), readmeResource.getContent());
        }

        return null;
    }
}
