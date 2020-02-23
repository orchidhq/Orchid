package com.eden.orchid.api.render;

import com.eden.common.util.EdenUtils;
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
    public FileRenderer(@Named("dest") String destination) {
        this.destination = destination;
    }

    public boolean render(OrchidPage page, InputStream content) {
        boolean success;

        // get the file that this page will be written to
        File outputFile = new File(this.destination + "/" + page.getReference().getPathOnDisk());

        // if its parent directory does not exist, make sure to create it
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        // write the page's contents to disk
        try {
            Files.write(outputFile.toPath(), IOUtils.toByteArray(content));
            success = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

}
