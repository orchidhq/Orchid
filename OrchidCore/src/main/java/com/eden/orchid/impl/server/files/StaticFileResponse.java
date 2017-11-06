package com.eden.orchid.impl.server.files;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.server.OrchidResponse;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.io.FilenameUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public final class StaticFileResponse {

    private final OrchidContext context;

    public static Map<String, String> mimeTypes = new HashMap<>();

    static {
        mimeTypes.put("html", "text/html");
        mimeTypes.put("htm",  "text/html");
        mimeTypes.put("css",  "text/css");
        mimeTypes.put("js",   "application/javascript");
        mimeTypes.put("json", "application/json");
        mimeTypes.put("svg",  "image/svg+xml");
        mimeTypes.put("png",  "image/png");
        mimeTypes.put("jpg",  "image/jpeg");
    }

    @Inject
    public StaticFileResponse(OrchidContext context) {
        this.context = context;
    }

    public OrchidResponse getResponse(File targetFile, String targetPath) {
        //TODO: attempt to use NanoHttpd facilities for guessing MIME type
        String mimeType = mimeTypes.getOrDefault(FilenameUtils.getExtension(targetFile.getName()), "text/plain");

        Clog.i("Rendering File: #{$1}", targetPath);
        try {
            return new OrchidResponse(NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mimeType, new FileInputStream(targetFile), targetFile.length()));
        }
        catch (Exception e) {
            return new OrchidResponse(NanoHTTPD.newFixedLengthResponse("Something went wrong opening file: " + targetPath));
        }
    }
}
