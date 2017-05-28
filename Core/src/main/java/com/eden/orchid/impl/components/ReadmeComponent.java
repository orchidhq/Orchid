package com.eden.orchid.impl.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.utilities.FileLoader;

import javax.inject.Inject;
import java.util.Set;

public class ReadmeComponent extends OrchidComponent {

    private FileLoader fileLoader;

    @Inject
    public ReadmeComponent(
            OrchidContext context,
            Set<TemplateResolutionStrategy> strategies,
            OrchidResources resources,
            FileLoader fileLoader) {
        super(context, strategies, resources);
        this.alias = "readme";
        this.fileLoader = fileLoader;
    }

    @Override
    public String render() {
        OrchidResource readmeResource = fileLoader.findClosestFile("readme");
        if (readmeResource != null) {
            return renderString(readmeResource.getReference().getExtension(), readmeResource.getContent());
        }

        return null;
    }
}
