package com.eden.orchid.api.server.files;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.server.OrchidResponse;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

public final class StaticFileResponse {

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

    public OrchidResponse getResponse(OrchidContext context, File targetFile, String targetPath) {
        String mimeType = mimeTypes.getOrDefault(FilenameUtils.getExtension(targetFile.getName()), NanoHTTPD.getMimeTypeForFile(targetFile.getName()));

        Clog.i("Rendering File: #{$1}", targetPath);
        try {
            return new OrchidResponse(context)
                    .contentStream(new FileInputStream(targetFile), targetFile.length())
                    .mimeType(mimeType);
        }
        catch (Exception e) {
            return new OrchidResponse(context).content("Something went wrong opening file: " + targetPath);
        }
    }
}
