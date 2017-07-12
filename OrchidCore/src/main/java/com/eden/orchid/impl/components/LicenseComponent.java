package com.eden.orchid.impl.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;

public class LicenseComponent extends OrchidComponent {

    @Inject
    public LicenseComponent(
            OrchidContext context,
            OrchidResources resources,
            TemplateResolutionStrategy strategy) {
        super(context, resources, strategy);
        this.alias = "license";
    }

    @Override
    public String getContent() {
        OrchidResource readmeResource = resources.findClosestFile("license");
        if (readmeResource != null) {
            return renderString(readmeResource.getReference().getExtension(), readmeResource.getContent());
        }

        return null;
    }
}
