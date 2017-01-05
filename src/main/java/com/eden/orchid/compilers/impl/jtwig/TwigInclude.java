package com.eden.orchid.compilers.impl.jtwig;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.utilities.resources.OrchidEntry;
import com.eden.orchid.utilities.resources.OrchidResources;
import com.google.common.base.Optional;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.jtwig.resource.loader.ResourceLoader;
import org.jtwig.resource.loader.TypedResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TwigInclude implements ResourceLoader {

    static {
        JTwigCompiler.config.resources().resourceLoaders()
                            .add(new TypedResourceLoader("orchid", new TwigInclude()))
                            .filter(input -> input.getType().equals("string") || input.getType().equals("orchid"));
    }

    @Override
    public Optional<Charset> getCharset(String path) {
        return Optional.of(Charset.defaultCharset());
    }

    @Override
    public InputStream load(String path) {
        Clog.v("Loading path from Twig: " + path);

        OrchidEntry entry;
        if(path.startsWith("/")) {
            entry = OrchidResources.getResourceEntry(path);
        }
        else {
            entry = OrchidResources.getResourceEntry("templates_oldd/includes/" + path);
        }

        String content = entry.getContent();

        if(!FilenameUtils.getExtension(entry.getFileName()).endsWith(".twig") || !FilenameUtils.getExtension(entry.getFileName()).endsWith(".html")) {
            content = Orchid.getTheme().compile(FilenameUtils.getExtension(entry.getFileName()), content, new JSONObject(Orchid.getRoot().toMap()));
        }

        return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean exists(String path) {
        Clog.v("Checking path from Twig: " + path);

        OrchidEntry entry;
        if(path.startsWith("/")) {
            entry = OrchidResources.getResourceEntry(path);
        }
        else {
            entry = OrchidResources.getResourceEntry("templates_oldd/includes/" + path);
        }

        return entry != null;
    }

    @Override
    public Optional<URL> toUrl(String path) {
        return null;
    }
}
