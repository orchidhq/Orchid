package com.eden.orchid.api.resources.resource;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OrchidFlags;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.utilities.OrchidExtensionsKt;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.text.StringsKt;
import org.apache.commons.io.IOUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * A Resource type that provides a content to a template from a resource file on disk. When used with
 * renderTemplate() or renderString(), this resource will supply the `page.content` variable to the template renderer as
 * the file contents after having the embedded data removed, and any embedded data will be available in the renderer
 * through the `page` variable. When used with renderRaw(), the raw contents (after having the embedded data removed)
 * will be written directly instead.
 */
public final class FileResource extends FreeableResource {

    private final File file;

    public FileResource(OrchidContext context, File file) {
        this(file, new OrchidReference(context, FileResource.pathFromFile(context, file)));
    }

    public FileResource(OrchidContext context, File file, File baseDirectory) {
        this(file, new OrchidReference(context, FileResource.pathFromFile(context, file, baseDirectory.getAbsolutePath())));
    }

    public FileResource(File file, OrchidReference reference) {
        super(reference);
        this.file = file;
    }

    private static String pathFromFile(OrchidContext context, File file) {
        return pathFromFile(context, file, OrchidFlags.getInstance().getFlagValue("src"));
    }

    private static String pathFromFile(OrchidContext context, File file, String basePath) {
        String filePath = file.getPath();

        // normalise Windows-style backslashes to common forward slashes
        basePath = basePath.replaceAll("\\\\", "/");
        filePath = filePath.replaceAll("\\\\", "/");

        // Remove the common base path from the actual file path
        if (filePath.startsWith(basePath)) {
            filePath = filePath.replaceAll(basePath, "");
        }

        if (filePath.startsWith("/")) {
            filePath = StringsKt.removePrefix(filePath, "/");
        }

        // if the path is not a child of the base path (i.e. still has relative path segments), strip those away. The
        // resolved "path" of this resource will be the portion after those relative segments.

        filePath = CollectionsKt.joinToString(
                ArraysKt.filter(filePath.split("/"), (it) -> !(it.equals("..") || it.equals("."))),
                "/", "", "", -1, "", null);

        return filePath;
    }

    @Override
    @NotNull
    public InputStream getContentStream() {
        try {
            return new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            return OrchidExtensionsKt.asInputStream("");
        }
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public boolean canDelete() {
        return true;
    }

    @Override
    public void update(InputStream newContent) throws IOException {
        if (file != null && newContent != null) {
            Files.write(file.toPath(), IOUtils.toByteArray(newContent));
        }
    }

    @Override
    public void delete() throws IOException {
        if (file != null) {
            file.delete();
        }
    }
}
