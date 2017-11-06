package com.eden.orchid.impl.server.files;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.server.OrchidResponse;
import com.eden.orchid.api.theme.AdminTheme;
import com.eden.orchid.api.theme.pages.OrchidPage;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.io.FilenameUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;

public final class AdminAssetResponse {

    private final OrchidContext context;

    @Inject
    public AdminAssetResponse(OrchidContext context) {
        this.context = context;
    }

    public OrchidResponse getResponse(File targetFile, String targetPath) {
        AdminTheme theme = context.getAdminTheme();

        OrchidResource res = theme.getResourceEntry(targetPath);
        String mimeType = StaticFileResponse.mimeTypes.getOrDefault(FilenameUtils.getExtension(targetFile.getName()), "text/plain");

        Clog.i("Rendering admin File: #{$1}", targetPath);
        if (res != null) {

            if(context.isBinaryExtension(FilenameUtils.getExtension(targetFile.getName()))) {
                try {
                    InputStream stream = res.getContentStream();
                    return new OrchidResponse(NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mimeType, stream, stream.available()));
                }
                catch (Exception e) {
                    return new OrchidResponse(NanoHTTPD.newFixedLengthResponse("Something went wrong opening file: " + targetPath));
                }
            }
            else {
                OrchidPage page = new OrchidPage(res, "");
                return new OrchidResponse(NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mimeType, page.getContent()));
            }
        }

        return null;
    }
}
