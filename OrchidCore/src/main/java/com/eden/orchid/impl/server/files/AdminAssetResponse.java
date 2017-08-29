package com.eden.orchid.impl.server.files;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.AdminTheme;
import com.eden.orchid.api.theme.pages.OrchidPage;
import fi.iki.elonen.NanoHTTPD;

import javax.inject.Inject;
import java.io.File;

public class AdminAssetResponse {

    private OrchidContext context;

    @Inject
    public AdminAssetResponse(OrchidContext context) {
        this.context = context;
    }

    public NanoHTTPD.Response getResponse(File targetFile, String targetPath) {
        AdminTheme theme = context.getAdminTheme();

        OrchidResource res = theme.getResourceEntry(targetPath);
        if(res != null) {
            OrchidPage page = new OrchidPage(res, "");
            return NanoHTTPD.newFixedLengthResponse(page.getContent());
        }
        return null;
    }
}
