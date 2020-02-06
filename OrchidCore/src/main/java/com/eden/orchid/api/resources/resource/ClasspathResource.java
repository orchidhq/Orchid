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
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;

/**
 * A Resource type that provides a content from a resource available on the Classpath. Unlike {@link JarResource},
 * classpath resources
 */
public final class ClasspathResource extends FreeableResource {

    private final URL classpathResource;

    public ClasspathResource(OrchidContext context, URL classpathResource) {
        this(classpathResource, new OrchidReference(context, pathFromUrl(classpathResource)));
    }

    public ClasspathResource(URL classpathResource, OrchidReference reference) {
        super(reference);
        this.classpathResource = classpathResource;
    }

    private static String pathFromUrl(URL url) {
        return url.getPath();
    }

    @Override
    @NotNull
    public InputStream getContentStream() {
        try {
            return classpathResource.openStream();
        } catch (Exception e) {
            e.printStackTrace();
            return OrchidExtensionsKt.asInputStream("");
        }
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public boolean canDelete() {
        return false;
    }

}
