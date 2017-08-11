package com.eden.orchid.server.impl.controllers.files;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import fi.iki.elonen.NanoHTTPD;

import javax.inject.Inject;
import java.io.InputStream;

public class FaviconResponse {

    private OrchidContext context;
    private OrchidResources resources;

    @Inject
    public FaviconResponse(OrchidContext context, OrchidResources resources) {
        this.context = context;
        this.resources = resources;
    }

    public NanoHTTPD.Response getResponse(String targetPath) {
        try {
            OrchidResource faviconResource = this.resources.getResourceEntry("favicon.ico");
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
