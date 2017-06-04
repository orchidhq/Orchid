package com.eden.orchid.impl.render;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.name.Named;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class OrchidRendererImpl extends OrchidRenderer {

    private final String destination;

    @Inject
    public OrchidRendererImpl(
            OrchidContext context,
            @Named("d") String destination,
            OrchidResources resources,
            Set<TemplateResolutionStrategy> strategies) {
        super(context, resources, strategies);
        this.destination = destination;
    }

    protected boolean render(OrchidPage page, String extension, String content) {
        page.prepareComponents();
        content = "" + context.compile(extension, content, page);
        String outputPath   = OrchidUtils.normalizePath(page.getReference().getPath());
        String outputName   = OrchidUtils.normalizePath(page.getReference().getFileName()) + "." + OrchidUtils.normalizePath(page.getReference().getOutputExtension());

        File outputFile = new File(this.destination + "/" + outputPath);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }

        try {
            Path classesFile = Paths.get(this.destination + "/" + outputPath + "/" + outputName);
            Files.write(classesFile, content.getBytes());
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
