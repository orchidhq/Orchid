package com.eden.orchid.impl.compilers.pebble;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.utilities.OrchidUtils;
import com.mitchellbosecke.pebble.error.LoaderException;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.utils.PathUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

public class PebbleTemplateLoader implements Loader<String> {

    private final OrchidContext context;

    @Setter
    private String prefix;

    @Setter
    private String suffix;

    @Getter @Setter
    private String charset;

    @Inject
    public PebbleTemplateLoader(OrchidContext context) {
        this.context = context;
        this.charset = "UTF-8";
    }

    @Override
    public Reader getReader(String templateName) throws LoaderException {
        templateName = templateName.replaceAll("::.*::", "");

        boolean ignoreMissing = false;

        if(templateName.startsWith("?")) {
            ignoreMissing = true;
            templateName = StringUtils.stripStart(templateName, "?");
        }

        if(!templateName.contains(".")) {
            templateName = templateName + "." + context.getTheme().getPreferredTemplateExtension();
        }

        if(templateName.contains(",")) {
            String[] templates = templateName.split(",");

            for(String template : templates) {
                try {
                    Reader reader = getReaderFromSingleFile(template, false);
                    if(reader != null) {
                        return reader;
                    }
                }
                catch (LoaderException e) {

                }
            }

            if(ignoreMissing) {
                return new StringReader("");
            }
            else {
                throw new LoaderException(null, "Could not find template in list \"" + templateName + "\"");
            }
        }
        else {
            return getReaderFromSingleFile(templateName, ignoreMissing);
        }
    }

    @Override
    public String resolveRelativePath(String relativePath, String anchorPath) {
        return PathUtils.resolveRelativePath(relativePath, anchorPath, File.separatorChar);
    }

    @Override
    public String createCacheKey(String templateName) {
        return "::" + context.getTheme().getKey() + "::" + templateName;
    }

    public String getPrefix() {
        return OrchidUtils.normalizePath(prefix);
    }

    public String getSuffix() {
        return OrchidUtils.normalizePath(suffix);
    }

    private Reader getReaderFromSingleFile(String templateName, boolean ignoreMissing) throws LoaderException {
        templateName = OrchidUtils.normalizePath(templateName);

        InputStreamReader isr = null;
        Reader reader = null;

        InputStream is = null;

        // add the prefix and ensure the prefix ends with a separator character
        StringBuilder path = new StringBuilder("");
        if (getPrefix() != null) {
            path.append(getPrefix());

            if (!getPrefix().endsWith("/")) {
                path.append("/");
            }
        }

        templateName = templateName + (getSuffix() == null ? "" : getSuffix());

        /*
         * if template name contains path segments, move those segments into the
         * path variable. The below technique needs to know the difference
         * between the path and file name.
         */
        String[] pathSegments = templateName.split("/");

        if (pathSegments.length > 1) {
            // file name is the last segment
            templateName = pathSegments[pathSegments.length - 1];
        }
        for (int i = 0; i < (pathSegments.length - 1); i++) {
            path.append(pathSegments[i]).append(File.separatorChar);
        }

        String fullFileName = OrchidUtils.normalizePath(path.toString() + templateName);

        if(!fullFileName.startsWith("templates/")) {
            fullFileName = "templates/" + fullFileName;
        }

        // try to load File
        if (is == null) {
            OrchidResource resource = context.getResourceEntry(fullFileName);
            if (resource != null) {
                is = resource.getContentStream();
            }
        }

        if (is == null) {
            if(ignoreMissing) {
                return new StringReader("");
            }
            else {
                throw new LoaderException(null, "Could not find template \"" + fullFileName + "\"");
            }
        }

        try {
            isr = new InputStreamReader(is, charset);
            reader = new BufferedReader(isr);
        }
        catch (UnsupportedEncodingException e) {
        }

        return reader;
    }

}
