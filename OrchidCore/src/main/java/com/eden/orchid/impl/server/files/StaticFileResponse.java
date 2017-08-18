package com.eden.orchid.impl.server.files;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.io.FilenameUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public class StaticFileResponse {

    private OrchidContext context;

    private Map<String, String> mimeTypes;

    @Inject
    public StaticFileResponse(OrchidContext context) {
        this.context = context;

        mimeTypes = new HashMap<>();
        mimeTypes.put("html", "text/html");
        mimeTypes.put("htm",  "text/html");
        mimeTypes.put("css",  "text/css");
        mimeTypes.put("js",   "application/javascript");
        mimeTypes.put("json", "application/json");
        mimeTypes.put("svg",  "image/svg+xml");
    }

    public NanoHTTPD.Response getResponse(File targetFile, String targetPath) {
        //TODO: attempt to use NanoHttpd facilities for guessing MIME type
        String mimeType = mimeTypes.getOrDefault(FilenameUtils.getExtension(targetFile.getName()), "text/plain");

        Clog.i("Rendering File: #{$1}", targetPath);
        try {
            return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mimeType, new FileInputStream(targetFile), targetFile.length());
        }
        catch (Exception e) {
            return NanoHTTPD.newFixedLengthResponse("Something went wrong opening file: " + targetPath);
        }
    }
}
