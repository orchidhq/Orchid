package com.eden.orchid.impl.render;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;

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
        page.prepareComponents();
        content = "" + context.getTheme().compile(extension, content, page);
        String outputPath   = OrchidUtils.normalizePath(page.getReference().getFullPath());
        String outputName   = OrchidUtils.normalizePath(page.getReference().getFileName()) + "." + OrchidUtils.normalizePath(page.getReference().getOutputExtension());
        String baseDir      = OrchidUtils.normalizePath(context.query("options.d").getElement().toString());
        String resourcesDir = OrchidUtils.normalizePath(context.query("options.resourcesDir").getElement().toString());

        // TODO: find out why this line is even necessary. Somwhere a base path is not being stripped properly...
        outputPath = baseDir + "/" + outputPath.replace(resourcesDir, "").replace(baseDir, "");
        if(baseDir.startsWith("/")) {
            outputPath = "/" + outputPath;
        }
        outputPath = OrchidUtils.normalizePath(outputPath);
        Clog.v("Output dir: '#{$1}', output path: '#{$2}'", new Object[]{baseDir, outputPath});
        // endTODO

        File outputFile = new File(outputPath);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }

        try {
            Path classesFile = Paths.get(outputPath + "/" + outputName);
            Files.write(classesFile, content.getBytes());
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
