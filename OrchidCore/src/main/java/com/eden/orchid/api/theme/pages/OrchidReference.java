package com.eden.orchid.api.theme.pages;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import java.net.URL;

public final class OrchidReference {

    protected final OrchidContext context;

    private final String originalFullFileName;
    private final String originalFileName;

    /**
     * The base URL of this reference, the URL at the root of your output site.
     */
    private String baseUrl;

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
     * The querystring for linking to external pages
     */
    private String query;

    /**
     * The output extension of the file.
     */
    private String outputExtension;

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
    private boolean usePrettyUrl = true;

    public OrchidReference(OrchidContext context, String fullFileName, boolean shouldParse) {
        this.context = context;
        baseUrl = context.getBaseUrl();
        if(fullFileName == null) {
            fullFileName = "";
        }
        fullFileName = fullFileName.trim();
        originalFullFileName = OrchidUtils.normalizePath(fullFileName);
        originalFileName = FilenameUtils.getBaseName(originalFullFileName);

        if(shouldParse) {
            parseFullFilename(fullFileName);
        }
    }

    public OrchidReference(OrchidContext context, String fullFileName) {
        this(context, fullFileName, true);
    }

    public OrchidReference(OrchidReference source) {
        this.context = source.context;
        this.baseUrl = source.baseUrl;
        this.originalFullFileName = source.originalFullFileName;
        this.originalFileName = source.originalFileName;
        this.path = source.path;
        this.fileName = source.fileName;
        this.extension = source.extension;
        this.id = source.id;
        this.query = source.query;
        this.title = source.title;
        this.usePrettyUrl = source.usePrettyUrl;
    }

    private void parseFullFilename(String fullFileName) {
        String partToTrim;

        if (fullFileName.contains(".")) {
            String[] parts = fullFileName.split("\\.");

            if(parts.length >= 3 && !context.isIgnoredOutputExtension(parts[parts.length - 2])) {
                this.extension = parts[parts.length - 1];
                this.outputExtension = parts[parts.length - 2];
                partToTrim = this.outputExtension + "." + this.extension;
            }
            else {
                this.extension = parts[parts.length - 1];
                this.outputExtension = null;
                partToTrim = this.extension;
            }
        }
        else {
            this.extension = "";
            partToTrim = "";
        }

        if (fullFileName.contains("/")) {
            String[] parts = fullFileName.split("/");

            path = "";
            for (int i = 0; i < parts.length; i++) {
                if (i == parts.length - 1) {
                    fileName = parts[i].replace("." + partToTrim, "");
                }
                else {
                    path += parts[i] + "/";
                }
            }
        }
        else {
            path = "";
            fileName = fullFileName.replace("." + partToTrim, "");
        }

        path = OrchidUtils.normalizePath(path);
    }

    public void stripFromPath(String pathToStrip) {
        pathToStrip = OrchidUtils.normalizePath(pathToStrip);

        if (this.path.startsWith(pathToStrip)) {
            this.path = OrchidUtils.normalizePath(path.replace(pathToStrip, ""));
        }
    }

    public void replacePathSegment(int segmentIndex, String replacement) {
        String[] segments = getOriginalPath().split("/");
        segments[segmentIndex] = OrchidUtils.normalizePath(replacement);
        this.path = String.join("/", segments);
    }

    public void removePathSegment(int segmentIndex) {
        this.path = String.join("/", ArrayUtils.remove(getOriginalPath().split("/"), segmentIndex));
    }

    public void setAsDirectoryIndex() {
        String[] segments = getOriginalPathSegments();
        fileName = segments[segments.length - 1];
        removePathSegment(segments.length - 1);
    }

    public String getOriginalPath() {
        return path;
    }

    public String getOriginalFullFileName() {
        return originalFullFileName;
    }

    public String getOriginalPathSegment(int segmentIndex) {
        String path = getOriginalPath();
        String[] segments = path.split("/");
        return segments[segmentIndex];
    }

    public String getPath() {
        String output = "";

        if (usePrettyUrl) {
            if (!EdenUtils.isEmpty(path)) {
                output += path;
                if (!path.endsWith("/")) {
                    output += "/";
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

    public String getPathSegment(int segmentIndex) {
        String path = getPath();
        String[] segments = path.split("/");

        final int actualIndex;
        // we have a positive index
        if(segmentIndex >= 0 && segmentIndex <= segments.length - 1) {
            actualIndex = segmentIndex;
        }
        // we have a negative index, search from end
        else if(segmentIndex < 0 && Math.abs(segmentIndex) <= segments.length) {
            actualIndex = segments.length - Math.abs(segmentIndex);
        }

        // index not in range
        else {
            throw new ArrayIndexOutOfBoundsException(segmentIndex);
        }

        return segments[actualIndex];
    }

    public String[] getPathSegments() {
        return getPath().split("/");
    }

    public String[] getOriginalPathSegments() {
        return getOriginalPath().split("/");
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public String getFileName() {
        if (usePrettyUrl) {
            return "index";
        }
        else {
            return fileName;
        }
    }

    public String getOutputExtension() {
        return (this.outputExtension != null) ? this.outputExtension : context.getOutputExtension(extension);
    }

    public String getTitle() {
        if (!EdenUtils.isEmpty(title)) {
            return title;
        }
        else {
            return fileName;
        }
    }

    public String getServerPath() {
        String output = "";

        if (!EdenUtils.isEmpty(baseUrl)) {
            output += baseUrl;
            if (!baseUrl.endsWith("/")) {
                output += "/";
            }
        }
        else {
            output += "/";
        }

        if (!EdenUtils.isEmpty(path)) {
            output += path;
            if (!path.endsWith("/")) {
                output += "/";
            }
        }

        if (!EdenUtils.isEmpty(fileName)) {
            output += fileName;

            if (!usePrettyUrl && !EdenUtils.isEmpty(getOutputExtension())) {
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

        if (!EdenUtils.isEmpty(query)) {
            output += "?";
            output += query;
        }

        return OrchidUtils.normalizePath(output);
    }

    public String getRelativePath() {
        String output = "";

        if (!EdenUtils.isEmpty(path)) {
            output += path;
            if (!path.endsWith("/")) {
                output += "/";
            }
        }

        if (!EdenUtils.isEmpty(fileName)) {
            output += fileName;

            if (!usePrettyUrl && !EdenUtils.isEmpty(getOutputExtension())) {
                output += ".";
                if (!EdenUtils.isEmpty(extension)) {
                    output += extension;
                }
            }
        }

        return output;
    }

    @Override
    public String toString() {
        return getServerPath();
    }

    public OrchidContext getContext() {
        return context;
    }

    public JSONObject toJSON() {
        JSONObject referenceJson = new JSONObject();
        referenceJson.put("link", this.toString());

        referenceJson.put("baseUrl", this.baseUrl);
        referenceJson.put("path", this.path);
        referenceJson.put("fileName", this.fileName);
        referenceJson.put("extension", this.extension);
        referenceJson.put("id", this.id);
        referenceJson.put("query", this.query);
        referenceJson.put("title", this.title);
        referenceJson.put("usePrettyUrl", this.usePrettyUrl);

        return referenceJson;
    }

    public static OrchidReference fromJSON(OrchidContext context, JSONObject source) {
        OrchidReference newReference = new OrchidReference(context, null, false);
        newReference.baseUrl = source.optString("baseUrl");
        newReference.path = source.optString("path");
        newReference.fileName = source.optString("fileName");
        newReference.extension = source.optString("extension");
        newReference.id = source.optString("id");
        newReference.query = source.optString("query");
        newReference.title = source.optString("title");
        newReference.usePrettyUrl = source.optBoolean("usePrettyUrl");
        return newReference;
    }

    public static OrchidReference fromUrl(OrchidContext context, String title, String url) {
        try {
            URL parsedUrl = new URL(url);
            OrchidReference newReference = new OrchidReference(context, url, false);

            if(parsedUrl.getPort() != -1) {
                newReference.baseUrl = parsedUrl.getProtocol() + "://" + parsedUrl.getHost() + ":" + parsedUrl.getPort();
            }
            else {
                newReference.baseUrl = parsedUrl.getProtocol() + "://" + parsedUrl.getHost();
            }

            newReference.fileName = FilenameUtils.removeExtension(FilenameUtils.getName(parsedUrl.getPath()));
            newReference.path = OrchidUtils.normalizePath(parsedUrl.getPath().replaceAll(FilenameUtils.getName(parsedUrl.getPath()), ""));
            newReference.title = title;
            newReference.extension = FilenameUtils.getExtension(FilenameUtils.getName(url));
            newReference.outputExtension = newReference.extension;
            newReference.query = parsedUrl.getQuery();
            newReference.id = parsedUrl.getRef();
            newReference.setUsePrettyUrl(false);

            return newReference;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

// Delombok
//----------------------------------------------------------------------------------------------------------------------

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public String getExtension() {
        return this.extension;
    }

    public String getId() {
        return this.id;
    }

    public boolean isUsePrettyUrl() {
        return this.usePrettyUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setOutputExtension(String outputExtension) {
        this.outputExtension = outputExtension;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUsePrettyUrl(boolean usePrettyUrl) {
        this.usePrettyUrl = usePrettyUrl;
    }

    public String getQuery() {
        return query;
    }

    public OrchidReference setQuery(String query) {
        this.query = query;
        return this;
    }
}

