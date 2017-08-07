package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.name.Named;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TwigRenderer extends OrchidRenderer {

    private final String destination;

    @Inject
    public TwigRenderer(@Named("d") String destination, OrchidContext context, OrchidResources resources, TemplateResolutionStrategy strategy) {
        super(context, resources, strategy);
        this.destination = destination;
    }

    protected boolean render(OrchidPage page, String extension, String content) {
        page.setCurrent(true);
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
            page.setCurrent(false);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            page.setCurrent(false);
            return false;
        }
    }

    protected boolean render(OrchidPage page, String extension, InputStream content) {
        page.setCurrent(true);
        String outputPath   = OrchidUtils.normalizePath(page.getReference().getPath());
        String outputName   = OrchidUtils.normalizePath(page.getReference().getFileName()) + "." + OrchidUtils.normalizePath(page.getReference().getOutputExtension());

        File outputFile = new File(this.destination + "/" + outputPath);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }

        try {
            Path classesFile = Paths.get(this.destination + "/" + outputPath + "/" + outputName);
            Files.write(classesFile, IOUtils.toByteArray(content));
            page.setCurrent(false);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            page.setCurrent(false);
            return false;
        }
    }
}
