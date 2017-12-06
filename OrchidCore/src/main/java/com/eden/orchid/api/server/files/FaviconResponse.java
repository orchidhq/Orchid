package com.eden.orchid.api.server.files;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.server.OrchidResponse;

import javax.inject.Inject;
import java.io.InputStream;

public final class FaviconResponse {

    private final OrchidContext context;

    @Inject
    public FaviconResponse(OrchidContext context) {
        this.context = context;
    }

    public OrchidResponse getResponse(String targetPath) {
        try {
            OrchidResource faviconResource = context.getResourceEntry("favicon.ico");
            if(faviconResource != null) {
                InputStream is = faviconResource.getContentStream();

                if(is != null) {
                    return new OrchidResponse(context)
                            .contentStream(is)
                            .mimeType("image/x-icon");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return new OrchidResponse(context)
                .content("")
                .status(404)
                .mimeType("image/x-icon");
    }
}
