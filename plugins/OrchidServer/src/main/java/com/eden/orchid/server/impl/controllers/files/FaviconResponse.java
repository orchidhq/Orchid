package com.eden.orchid.server.impl.controllers.files;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import fi.iki.elonen.NanoHTTPD;

import javax.inject.Inject;
import java.io.InputStream;

public class FaviconResponse {

    private OrchidContext context;

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
