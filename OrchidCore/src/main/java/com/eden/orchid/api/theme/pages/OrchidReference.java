package com.eden.orchid.api.theme.pages;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.net.URL;

@Getter @Setter
public final class OrchidReference {

    protected OrchidContext context;

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

    public OrchidReference(OrchidContext context) {
        this.context = context;

        baseUrl = context.getBaseUrl();
    }

    public OrchidReference(OrchidContext context, String fullFileName) {
        this(context);

        if (fullFileName != null) {
            fullFileName = fullFileName.trim();
        }

        if (fullFileName.contains(".")) {
            String[] parts = fullFileName.split("\\.");
            this.extension = parts[parts.length - 1];
        }
        else {
            this.extension = "";
        }

        if (fullFileName.contains("/")) {
            String[] parts = fullFileName.split("/");

            path = "";
            for (int i = 0; i < parts.length; i++) {
                if (i == parts.length - 1) {
                    fileName = parts[i].replace("." + extension, "");
                }
                else {
                    path += parts[i] + "/";
                }
            }
        }
        else {
            path = "";
            fileName = fullFileName.replace("." + extension, "");
        }

        path = OrchidUtils.normalizePath(path);
    }

    public OrchidReference(OrchidContext context, OrchidReference source) {
        this.context = context;
        this.baseUrl = source.baseUrl;
        this.path = source.path;
        this.fileName = source.fileName;
        this.extension = source.extension;
        this.id = source.id;
        this.title = source.title;
        this.usePrettyUrl = source.usePrettyUrl;
    }

    public void stripFromPath(String pathToStrip) {
        pathToStrip = OrchidUtils.normalizePath(pathToStrip);

        if (this.path.startsWith(pathToStrip)) {
            this.path = OrchidUtils.normalizePath(path.replace(pathToStrip, ""));
        }
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

    public String getFileName() {
        if (usePrettyUrl) {
            return "index";
        }
        else {
            return fileName;
        }
    }

    public String getOutputExtension() {
        return context.getOutputExtension(extension);
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

//    public String getPathOnDisk() {
//        String output = "";
//
//        if (!EdenUtils.isEmpty(basePath)) {
//            output += basePath;
//
//            if (!basePath.endsWith("/")) {
//                output += "/";
//            }
//        }
//
//        if (!EdenUtils.isEmpty(path)) {
//            output += path;
//            if (!path.endsWith("/")) {
//                output += "/";
//            }
//        }
//
//        if (!EdenUtils.isEmpty(fileName)) {
//            output += fileName;
//
//            if (!usePrettyUrl) {
//                output += ".";
//                if (!EdenUtils.isEmpty(extension)) {
//                    output += extension;
//                }
//            }
//        }
//
//        return output;
//    }

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

            if (!usePrettyUrl) {
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
        referenceJson.put("title", this.title);
        referenceJson.put("usePrettyUrl", this.usePrettyUrl);

        return referenceJson;
    }

    public static OrchidReference fromJSON(OrchidContext context, JSONObject source) {
        OrchidReference newReference = new OrchidReference(context);
        newReference.baseUrl = source.optString("baseUrl");
        newReference.path = source.optString("path");
        newReference.fileName = source.optString("fileName");
        newReference.extension = source.optString("extension");
        newReference.id = source.optString("id");
        newReference.title = source.optString("title");
        newReference.usePrettyUrl = source.optBoolean("usePrettyUrl");
        return newReference;
    }

    public static OrchidReference fromUrl(OrchidContext context, String title, String url) {
        try {
            URL parsedUrl = new URL(url);
            OrchidReference newReference = new OrchidReference(context);

            if(parsedUrl.getPort() != -1) {
                newReference.baseUrl = parsedUrl.getProtocol() + "://" + parsedUrl.getHost() + ":" + parsedUrl.getPort();
            }
            else {
                newReference.baseUrl = parsedUrl.getProtocol() + "://" + parsedUrl.getHost();
            }

            newReference.path = parsedUrl.getPath();
            newReference.fileName = "";
            newReference.title = title;
            newReference.extension = "";
            return newReference;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
