package com.eden.orchid.impl.compilers.pebble;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.Theme;
import com.mitchellbosecke.pebble.error.LoaderException;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.utils.PathUtils;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public final class PebbleTemplateLoader implements Loader<String> {
    private String charset;
    private final OrchidContext context;
    private final Theme theme;

    @Inject
    public PebbleTemplateLoader(OrchidContext context, Theme theme) {
        this.context = context;
        this.theme = theme;
        this.charset = "UTF-8";
    }

    @Override
    public Reader getReader(String templateName) throws LoaderException {
        if(context == null) {
            throw new LoaderException(null, "PebbleTemplateLoader is not initialized, context are null");
        }

        templateName = templateName.replaceAll("::.*::", "");
        try {
            OrchidResource res = context.locateTemplate(theme, templateName);
            return (res != null) ? getReaderFromResource(res) : new StringReader("");
        } catch (Exception e) {
            throw new LoaderException(e, "Could not find template in list \"" + templateName + "\"");
        }
    }

    @Override
    public String createCacheKey(String templateName) {
        if(theme != null) {
            return "::" + theme.getKey() + "::" + templateName;
        }
        else {
            return "::no_theme_configured::" + templateName;
        }
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
