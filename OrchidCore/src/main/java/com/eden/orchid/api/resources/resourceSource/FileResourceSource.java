package com.eden.orchid.api.resources.resourceSource;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.FileResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.google.inject.Provider;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileResourceSource implements OrchidResourceSource {

    private final Provider<OrchidContext> context;

    @Getter
    private final String directory;

    @Getter
    private final int priority;

    public FileResourceSource(Provider<OrchidContext> context, int priority) {
        this.context = context;
        this.directory = "";
        this.priority = priority;
    }

    public FileResourceSource(Provider<OrchidContext> context, String directory, int priority) {
        this.context = context;
        this.directory = directory;
        this.priority = priority;
    }

    @Override
    public OrchidResource getResourceEntry(String fileName) {
        File file = new File(directory + "/" + fileName);

        if (file.exists() && !file.isDirectory()) {
            return new FileResource(context.get(), file);
        }

        return null;
    }

    @Override
    public List<OrchidResource> getResourceEntries(String path, String[] fileExtensions, boolean recursive) {
        List<OrchidResource> entries = new ArrayList<>();

        String fullPath = directory + "/" + path;
        File file = new File(fullPath);

        if (file.exists() && file.isDirectory()) {
            Collection newFiles = FileUtils.listFiles(file, fileExtensions, recursive);

            if (!EdenUtils.isEmpty(newFiles)) {
                for (Object object : newFiles) {
                    File newFile = (File) object;
                    if (shouldAddEntry(newFile.getName())) {
                        FileResource entry = new FileResource(context.get(), newFile);
                        entry.setPriority(getPriority());
                        entries.add(entry);
                    }
                }
            }
        }

        return entries;
    }

    @Override
    public int compareTo(@NotNull OrchidResourceSource o) {
        if(o instanceof FileResourceSource) {
            int superValue = o.getPriority() - getPriority();

            if(superValue != 0) {
                return superValue;
            }
            return ((FileResourceSource) o).getDirectory().compareTo(getDirectory());
        }
        else {
            return o.getPriority() - getPriority();
        }
    }

}
