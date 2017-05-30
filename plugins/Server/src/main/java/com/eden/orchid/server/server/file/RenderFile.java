package com.eden.orchid.server.server.file;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidResources;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class RenderFile {

    private OrchidContext context;
    private OrchidResources resources;
    private RenderIndex renderIndex;

    private Map<String, String> mimeTypes;

    @Inject
    public RenderFile(OrchidContext context, OrchidResources resources, RenderIndex renderIndex) {
        this.context = context;
        this.resources = resources;
        this.renderIndex = renderIndex;

        mimeTypes = new HashMap<>();
        mimeTypes.put("html", "text/html; charset=UTF-8");
        mimeTypes.put("htm",  "text/html; charset=UTF-8");
        mimeTypes.put("css",  "text/css; charset=UTF-8");
        mimeTypes.put("js",   "application/javascript; charset=UTF-8");
        mimeTypes.put("json", "application/json; charset=UTF-8");
        mimeTypes.put("svg",  "image/svg+xml; charset=UTF-8");
    }

    public void render(HttpExchange t, File targetFile, String targetPath) throws IOException {
        Headers responseHeaders = t.getResponseHeaders();

        String mimeType = mimeTypes.containsKey(FilenameUtils.getExtension(targetFile.getName()))
                ? mimeTypes.get(FilenameUtils.getExtension(targetFile.getName()))
                : "text/plain; charset=UTF-8";

        responseHeaders.set("Content-Type", mimeType);

        Clog.i("Rendering File: #{$1}", new Object[]{targetPath});
        t.sendResponseHeaders(200, targetFile.length());
        IOUtils.write(IOUtils.toByteArray(new FileInputStream(targetFile)), t.getResponseBody());
        t.getResponseBody().close();
    }
}
