package com.eden.orchid.impl.server.files;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import fi.iki.elonen.NanoHTTPD;

import javax.inject.Inject;
import java.io.InputStream;

public final class FaviconResponse {

    private final OrchidContext context;

    @Inject
    public FaviconResponse(OrchidContext context) {
        this.context = context;
    }

    public NanoHTTPD.Response getResponse(String targetPath) {
        try {
            OrchidResource faviconResource = context.getResourceEntry("favicon.ico");
            if(faviconResource != null) {
                InputStream is = faviconResource.getContentStream();

                if(is != null) {
                    return NanoHTTPD.newChunkedResponse(NanoHTTPD.Response.Status.OK, "image/x-icon", is);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "image/x-icon", "");
    }
}
