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
                Clog.v("We have a favicon resource: " + faviconResource.getClass().toString());
                InputStream is = faviconResource.getContentStream();

                if(is != null) {
                    Clog.v("Returning favicon resource");

                    return NanoHTTPD.newChunkedResponse(NanoHTTPD.Response.Status.OK, "image/x-icon", is);
                }
                else {
                    Clog.v("Favicon resource stream was null");
                }
            }
            else {
                Clog.v("Favicon resource not found");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, "image/x-icon", "");
    }
}
