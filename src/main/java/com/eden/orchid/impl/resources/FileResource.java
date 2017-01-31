package com.eden.orchid.impl.resources;

import com.eden.orchid.resources.OrchidReference;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class FileResource extends FreeableResource  {

    private final File file;

    public FileResource(File file) {
        this(file, new OrchidReference(file.getPath()));
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
}
