package com.eden.orchid.impl.compilers.pebble;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.mitchellbosecke.pebble.error.LoaderException;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.utils.PathUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;

public final class PebbleTemplateLoader implements Loader<String> {
    private final Provider<OrchidContext> context;
    private String charset;

    @Inject
    public PebbleTemplateLoader(Provider<OrchidContext> context) {
        this.context = context;
        this.charset = "UTF-8";
    }

    @Override
    public Reader getReader(String templateName) throws LoaderException {
        templateName = templateName.replaceAll("::.*::", "");
        try {
            OrchidResource res = context.get().locateTemplate(templateName);
            return (res != null) ? getReaderFromResource(res) : new StringReader("");
        } catch (Exception e) {
            throw new LoaderException(e, "Could not find template in list \"" + templateName + "\"");
        }
    }

    @Override
    public String createCacheKey(String templateName) {
        return "::" + context.get().getTheme().getKey() + "::" + templateName;
    }

    private Reader getReaderFromResource(OrchidResource resource) {
        InputStream is = null;
        InputStreamReader isr = null;
        Reader reader = null;
        if (resource != null) {
            is = resource.getContentStream();
        }
        if (is != null) {
            isr = new InputStreamReader(is, Charset.forName("UTF-8"));
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
