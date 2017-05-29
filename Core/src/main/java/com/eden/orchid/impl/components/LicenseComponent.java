package com.eden.orchid.impl.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;
import java.util.Set;

public class LicenseComponent extends OrchidComponent {

    @Inject
    public LicenseComponent(
            OrchidContext context,
            Set<TemplateResolutionStrategy> strategies,
            OrchidResources resources) {
        super(context, strategies, resources);
        this.alias = "license";
    }

    @Override
    public String render() {
        OrchidResource readmeResource = resources.findClosestFile("license");
        if (readmeResource != null) {
            return renderString(readmeResource.getReference().getExtension(), readmeResource.getContent());
        }

        return null;
    }
}
