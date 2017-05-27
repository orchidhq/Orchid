package com.eden.orchid.impl.render;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.OrchidResources;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class OrchidRendererImpl extends OrchidRenderer {

    @Inject
    public OrchidRendererImpl(OrchidContext context, OrchidResources resources, Set<TemplateResolutionStrategy> strategies) {
        super(context, resources, strategies);
    }

    protected boolean render(OrchidPage page, String extension, String content) {
        content = "" + context.getTheme().compile(extension, content, page);

        String outputPath = page.getReference().getFullPath();
        String outputName = page.getReference().getFileName() + "." + page.getReference().getOutputExtension();

        outputPath = context.query("options.d").getElement().toString() + File.separator + outputPath.replaceAll("/", File.separator);

        File outputFile = new File(outputPath);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }

        try {
            Path classesFile = Paths.get(outputPath + File.separator + outputName);
            Files.write(classesFile, content.getBytes());
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
