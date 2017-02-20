package com.eden.orchid.impl.resources;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidResource;
import com.eden.orchid.api.resources.OrchidResourceSource;
import org.apache.commons.io.FileUtils;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class LocalResourceSource extends OrchidResourceSource {

    private String directory;
    private OrchidContext context;

    @Inject
    public LocalResourceSource(OrchidContext context) {
        this.context = context;

        setPriority(Integer.MAX_VALUE);
    }

    @Override
    public OrchidResource getResourceEntry(String fileName) {
        if (!EdenUtils.isEmpty(this.context.query("options.resourcesDir"))) {
            directory = this.context.query("options.resourcesDir").toString();
        }
        else {
            directory = "";
        }

        File file = new File(directory + File.separator + fileName.replaceAll("/", File.separator));

        if (file.exists() && !file.isDirectory()) {
            return new FileResource(file);
        }

        return null;
    }

    @Override
    public List<OrchidResource> getResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        if (!EdenUtils.isEmpty(this.context.query("options.resourcesDir"))) {
            directory = this.context.query("options.resourcesDir").toString();
        }
        else {
            directory = "";
        }

        List<OrchidResource> entries = new ArrayList<>();

        String fullPath = directory + File.separator + path;
        File file = new File(fullPath.replaceAll("/", File.separator));

        if (file.exists() && file.isDirectory()) {
            Collection newFiles = FileUtils.listFiles(file, fileExtensions, recursive);

            if (!EdenUtils.isEmpty(newFiles)) {
                for (Object object : newFiles) {
                    File newFile = (File) object;
                    if (shouldAddEntry(newFile.getName())) {
                        FileResource entry = new FileResource(newFile);
                        entry.setPriority(getPriority());
                        entries.add(entry);
                    }
                }
            }
        }

        return entries;
    }
}
