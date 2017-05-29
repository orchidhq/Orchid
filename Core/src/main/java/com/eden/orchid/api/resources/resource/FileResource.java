package com.eden.orchid.api.resources.resource;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * A Resource type that provides a content to a template from a resource file on disk. When used with
 * renderTemplate() or renderString(), this resource will supply the `page.content` variable to the template renderer as
 * the file contents after having the embedded data removed, and any embedded data will be available in the renderer
 * through the `page` variable. When used with renderRaw(), the raw contents (after having the embedded data removed)
 * will be written directly instead.
 */
public final class FileResource extends FreeableResource  {

    private final File file;

    public FileResource(OrchidContext context, File file) {
        this(file, new OrchidReference(context, FileResource.pathFromFile(context, file)));
    }

    public FileResource(File file, OrchidReference reference) {
        super(reference);

        this.file = file;
    }

    @Override
    protected void loadContent() {
        if(rawContent == null) {
            try {
                if (file != null) {
                    rawContent = IOUtils.toString(new FileInputStream(file));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.loadContent();
    }

    private static String pathFromFile(OrchidContext context, File file) {
        String filePath = file.getPath();

//        Clog.v("Creating OrchidReference: initial filePath='#{$1}'", new Object[]{filePath});

        if(OrchidUtils.elementIsString(context.query("options.resourcesDir"))) {
            String basePath = context.query("options.resourcesDir").getElement().toString();
            basePath = basePath.replaceAll("\\\\", "/");
            filePath = filePath.replaceAll("\\\\", "/");

//            Clog.v("Creating OrchidReference: basePath='#{$1}'", new Object[]{basePath});

            if(filePath.startsWith(basePath)) {
                filePath = filePath.replaceAll(basePath, "");
            }
        }

//        Clog.v("Creating OrchidReference: final filePath='#{$1}'", new Object[]{filePath});

        return filePath;
    }
}
