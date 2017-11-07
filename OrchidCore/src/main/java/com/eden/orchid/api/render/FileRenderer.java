package com.eden.orchid.api.render;

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

public final class FileRenderer implements OrchidRenderer {

    private final String destination;

    @Inject
    public FileRenderer(@Named("d") String destination) {
        this.destination = destination;
    }

    public boolean render(OrchidPage page, InputStream content) {
        boolean success;

        String outputPath = OrchidUtils.normalizePath(page.getReference().getPath());
        String outputName = OrchidUtils.normalizePath(page.getReference().getFileName()) + "." + OrchidUtils.normalizePath(page.getReference().getOutputExtension());

        File outputFile = new File(this.destination + "/" + outputPath);
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }

        try {
            Path classesFile = Paths.get(this.destination + "/" + outputPath + "/" + outputName);
            Files.write(classesFile, IOUtils.toByteArray(content));
            success = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

}
