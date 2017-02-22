package com.eden.orchid.api.resources;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.utilities.OrchidUtils;

import java.io.File;
import java.util.regex.Pattern;

public class OrchidReference {

    protected OrchidContext context;

    /**
     * The base URL of this reference, the URL at the root of your output site.
     */
    private String baseUrl;

    /**
     * The base path within the root of your site, useful for namespacing your output files to prevent conflicts.
     */
    private String basePath;

    /**
     * The path of the file within the basePath.
     */
    private String path;

    /**
     * The name of the file, not including its extension.
     */
    private String fileName;

    /**
     * The extension of the file.
     */
    private String extension;

    /**
     * An id for linking to a spot on the page.
     */
    private String id;

    /**
     * The title of this Reference, used for display purposes.
     */
    private String title;

    /**
     * Whether to use a 'pretty' url syntax. For example: 'www.example.com/pretty.html' with 'pretty' syntax moves the
     * fileName to the path and adds 'index.html' as the fileName, so the resulting url looks like 'www.example.com/pretty'
     */
    private boolean usePrettyUrl;

    public OrchidReference(OrchidContext context) {
        this.context = context;
        if (context.query("options.baseUrl") != null) {
            baseUrl = context.query("options.baseUrl").toString();
            baseUrl = OrchidUtils.stripSeparators(baseUrl);
        }
    }

    public OrchidReference(OrchidContext context, String fullFileName) {
        this(context);

        if (fullFileName.contains(".")) {
            String[] parts = fullFileName.split("\\.");
            this.extension = parts[parts.length - 1];
        }
        else {
            this.extension = "";
        }

        if (fullFileName.contains("/")) {
            String pattern = Pattern.quote(File.separator);
            String[] parts = fullFileName.split(pattern);

            path = "";
            for (int i = 0; i < parts.length; i++) {
                if (i == parts.length - 1) {
                    fileName = parts[i].replace("." + extension, "");
                }
                else {
                    path += parts[i] + File.separator;
                }
            }
        }
        else {
            path = "";
            fileName = fullFileName.replace("." + extension, "");
        }

        path = OrchidUtils.stripSeparators(path);

        if (context.query("options.resourcesDir") != null) {
            String basePath = context.query("options.resourcesDir").toString();
            basePath = OrchidUtils.stripSeparators(basePath);

            if (path.startsWith(basePath)) {
                path = path.replace(basePath, "");
                path = OrchidUtils.stripSeparators(path);
            }
        }
    }

    public OrchidReference(OrchidContext context, String basePath, String fullFileName) {
        this(context, fullFileName);

        this.basePath = basePath;

        if (this.path.startsWith(basePath)) {
            this.path = path.replace(basePath, "");
        }
    }

    public OrchidReference(OrchidContext context, OrchidReference source) {
        this.context = context;
        this.baseUrl = source.baseUrl;
        this.basePath = source.basePath;
        this.path = source.path;
        this.fileName = source.fileName;
        this.extension = source.extension;
        this.id = source.id;
        this.title = source.title;
        this.usePrettyUrl = source.usePrettyUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;

        if (this.path.startsWith(basePath)) {
            this.path = path.replace(basePath, "");
        }
    }

    public void stripBasePath(String basePath) {
        this.basePath = null;

        if (this.path.startsWith(basePath)) {
            this.path = path.replace(basePath, "");
        }
    }

    public String getPath() {
        String output = "";

        if (usePrettyUrl) {
            if (!EdenUtils.isEmpty(path)) {
                output += path;
                if (!path.endsWith(File.separator)) {
                    output += File.separator;
                }
            }

            if (!EdenUtils.isEmpty(fileName)) {
                output += fileName;
            }
        }
        else {
            if (!EdenUtils.isEmpty(path)) {
                output += path;
            }
        }

        return output;
    }

    public String getFullPath() {
        if (!EdenUtils.isEmpty(basePath)) {
            return basePath + File.separator + getPath();
        }
        else {
            return getPath();
        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        if (usePrettyUrl) {
            return "index";
        }
        else {
            return fileName;
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public String getOutputExtension() {
        return context.getTheme().getOutputExtension(extension);
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        if(!EdenUtils.isEmpty(title)) {
            return title;
        }
        else {
            return fileName;
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUsePrettyUrl() {
        return usePrettyUrl;
    }

    public void setUsePrettyUrl(boolean usePrettyUrl) {
        this.usePrettyUrl = usePrettyUrl;
    }

    @Override
    public String toString() {

        String output = "";

        if (!EdenUtils.isEmpty(baseUrl)) {
            output += baseUrl;
            if (!baseUrl.endsWith(File.separator)) {
                output += File.separator;
            }
        }
        else {
            output += File.separator;
        }

        if (!EdenUtils.isEmpty(basePath)) {
            output += basePath;

            if (!basePath.endsWith(File.separator)) {
                output += File.separator;
            }
        }

        if (!EdenUtils.isEmpty(path)) {
            output += path;
            if (!path.endsWith(File.separator)) {
                output += File.separator;
            }
        }

        if (!EdenUtils.isEmpty(fileName)) {
            output += fileName;

            if (!usePrettyUrl) {
                output += ".";
                if (!EdenUtils.isEmpty(extension)) {
                    output += extension;
                }
            }
        }

        if (!EdenUtils.isEmpty(id)) {
            output += "#";
            output += id;
        }

        return output;
    }

    public OrchidContext getContext() {
        return context;
    }
}
