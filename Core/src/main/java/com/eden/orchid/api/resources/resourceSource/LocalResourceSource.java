package com.eden.orchid.api.resources.resourceSource;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.resources.resource.FileResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import org.apache.commons.io.FileUtils;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class LocalResourceSource extends OrchidResourceSource {

    @Inject
    public LocalResourceSource() {

    }

    public abstract String getDirectory();

    @Override
    public OrchidResource getResourceEntry(String fileName) {
        File file = new File(getDirectory() + File.separator + fileName.replaceAll("/", File.separator));

        if (file.exists() && !file.isDirectory()) {
            return new FileResource(file);
        }

        return null;
    }

    @Override
    public List<OrchidResource> getResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        List<OrchidResource> entries = new ArrayList<>();

        String fullPath = getDirectory() + File.separator + path;
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
