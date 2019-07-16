package com.eden.orchid.api.server.files;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.server.OrchidResponse;
import com.eden.orchid.api.theme.assets.AssetHolder;
import com.eden.orchid.api.theme.assets.AssetHolderDelegate;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public final class NotFound404Response {

    public OrchidResponse getResponse(OrchidContext context, String targetPath) {
        AssetHolder assetHolder = new AssetHolderDelegate(context, null, null);
        String content = "";
        Clog.i("Rendering 404: #{$1}", targetPath);

        OrchidResource resource = context.locateLocalResourceEntry("404");
        OrchidPage page = null;
        if(resource != null) {
            page = new OrchidPage(
                    resource,
                    "404",
                    "Not Found!"
            );
        }
        else {
            resource = context.getResourceEntry("templates/server/404.peb");

            Map<String, Object> indexPageVars = new HashMap<>();
            indexPageVars.put("title", "Not Found - " + targetPath);
            indexPageVars.put("path", targetPath);

            Map<String, Object> object = new HashMap<>(context.getConfig());
            object.put("page", indexPageVars);
            object.put("theme", context.getTheme());

            String notFoundIndexContent;
            if (resource != null) {
                notFoundIndexContent = context.compile(resource.getReference().getExtension(), resource.getContent(), object);
            }
            else {
                notFoundIndexContent = context.serialize("json", object);
            }

            page = new OrchidPage(
                    new StringResource(context, "404.txt", notFoundIndexContent),
                    "404",
                    "Not Found!"
            );
            page.addJs("assets/js/shadowComponents.js");
            assetHolder.addCss("assets/css/directoryListing.css");
        }

        InputStream is = context.getRenderedTemplate(page);
        try {
            content = IOUtils.toString(is, Charset.forName("UTF-8"));
        }
        catch (Exception e) {
            content = "";
        }

        return new OrchidResponse(context).content(content).status(404);
    }
}
