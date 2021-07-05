package com.eden.orchid.api.server.files;

import clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.RenderService;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource;
import com.eden.orchid.api.server.OrchidResponse;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;

import java.util.HashMap;
import java.util.Map;

final class NotFound404Response {

    static OrchidResponse getResponse(OrchidContext context, String targetPath) {
        String content = "";
        Clog.i("Rendering 404: {}", targetPath);

        OrchidResource resource = context
                .getFlexibleResourceSource(LocalResourceSource.INSTANCE, null)
                .locateResourceEntry(context, "404");
        OrchidPage page = null;
        if(resource != null) {
            page = new OrchidPage(
                    resource,
                    RenderService.RenderMode.TEMPLATE,
                    "404",
                    "Not Found!"
            );
        }
        else {
            resource = context.getDefaultResourceSource(null, null).getResourceEntry(context, "templates/server/404.peb");

            Map<String, Object> indexPageVars = new HashMap<>();
            indexPageVars.put("title", "Not Found - " + targetPath);
            indexPageVars.put("path", targetPath);

            Map<String, Object> object = new HashMap<>(context.getConfig());
            object.put("page", indexPageVars);
            object.put("theme", context.getTheme());

            String notFoundIndexContent;
            if (resource != null) {
                notFoundIndexContent = context.compile(resource, resource.getReference().getExtension(), resource.getContent(), object);
            }
            else {
                notFoundIndexContent = context.serialize("json", object);
            }

            page = new OrchidPage(
                    new StringResource(new OrchidReference(context, "404.txt"), notFoundIndexContent),
                    RenderService.RenderMode.TEMPLATE,
                    "404",
                    "Not Found!"
            );
        }

        return new OrchidResponse(context).page(page).status(404);
    }
}
