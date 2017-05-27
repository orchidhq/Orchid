package com.eden.orchid.impl.components;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidComponent;
import com.eden.orchid.api.resources.resource.FileResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadmeComponent extends OrchidComponent {

    private OrchidContext context;

    @Inject
    public ReadmeComponent(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String render() {
        OrchidResource readmeResource = getProjectReadme();
        if (readmeResource != null) {
            return context.getTheme().compile(readmeResource.getReference().getExtension(), readmeResource.getContent());
        }

        return null;
    }

    /**
     * Find the projects README file
     *
     * @return an OrchidResource containing the README content if it was found, null otherwise
     */
    private OrchidResource getProjectReadme() {
        if (!EdenUtils.isEmpty(context.query("options.resourcesDir"))) {
            String resourceDir = context.query("options.resourcesDir").toString();

            File folder = new File(resourceDir);

            // set hard limit of searching no more than 10 parent directories for the README
            int maxIterations = 10;

            while (true) {
                if (folder.isDirectory()) {
                    List<File> files = new ArrayList<>(FileUtils.listFiles(folder, null, false));

                    for (File file : files) {
                        if (FilenameUtils.removeExtension(file.getName()).equalsIgnoreCase("readme")) {
                            return new FileResource(context, file);
                        }
                    }
                }

                // set the folder to its own parent and search again
                if (folder.getParentFile() != null && maxIterations > 0) {
                    folder = folder.getParentFile();
                    maxIterations--;
                }

                // there is no more parent to search, exit the loop
                else {
                    break;
                }
            }
        }

        return null;
    }
}
