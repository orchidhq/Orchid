package com.eden.orchid.impl.compilers.pebble;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resourcesource.OrchidResourceSource;
import com.eden.orchid.api.theme.AbstractTheme;
import com.mitchellbosecke.pebble.error.LoaderException;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.utils.PathUtils;
import kotlin.text.StringsKt;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public final class PebbleTemplateLoader implements Loader<String> {
    private final OrchidContext context;
    private final OrchidResourceSource templateResourceSource;
    private final String cacheKeyPrefix;
    private String charset;

    PebbleTemplateLoader(OrchidContext context, AbstractTheme theme) {
        this.context = context;
        this.cacheKeyPrefix = "::" + theme.getKey() + "::";
        this.templateResourceSource = context.getTemplateResourceSource(null, theme);
        this.charset = "UTF-8";
    }

    @Override
    public Reader getReader(String templateName) throws LoaderException {
        templateName = StringsKt.removePrefix(templateName, cacheKeyPrefix);
        try {
            OrchidResource res = templateResourceSource.getResourceEntry(context, templateName);
            return (res != null) ? getReaderFromResource(res) : new StringReader("");
        } catch (Exception e) {
            throw new LoaderException(e, "Could not find template in list \"" + templateName + "\"");
        }
    }

    @Override
    public String createCacheKey(String templateName) {
        return "::" + context.getTheme().getKey() + "::" + templateName;
    }

    @Override
    public boolean resourceExists(String templateName) {
        return true;
    }

    private Reader getReaderFromResource(OrchidResource resource) {
        InputStream is = null;
        InputStreamReader isr = null;
        Reader reader = null;
        if (resource != null) {
            is = resource.getContentStream();
        }
        if (is != null) {
            isr = new InputStreamReader(is, StandardCharsets.UTF_8);
            reader = new BufferedReader(isr);
        }
        return reader;
    }

    @Override
    public void setPrefix(String prefix) {
    }

    @Override
    public void setSuffix(String suffix) {
    }

    @Override
    public String resolveRelativePath(String relativePath, String anchorPath) {
        return PathUtils.resolveRelativePath(relativePath, anchorPath, File.separatorChar);
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(final String charset) {
        this.charset = charset;
    }
}
