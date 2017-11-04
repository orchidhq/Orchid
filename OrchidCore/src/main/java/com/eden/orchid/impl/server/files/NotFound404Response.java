package com.eden.orchid.impl.server.files;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.render.TemplateResolutionStrategy;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;

import javax.inject.Inject;

public final class NotFound404Response {

    private final OrchidContext context;
    private final TemplateResolutionStrategy strategy;

    @Inject
    public NotFound404Response(OrchidContext context, TemplateResolutionStrategy strategy) {
        this.context = context;
        this.strategy = strategy;
    }

    public NanoHTTPD.Response getResponse(String targetPath) {
        String content = "";
        Clog.i("Rendering 404: #{$1}", targetPath);

        OrchidResource resource = context.getResourceEntry("templates/server/404.twig");

        JSONObject indexPageVars = new JSONObject();
        indexPageVars.put("title", "Not Found - " + targetPath);
        indexPageVars.put("path", targetPath);

        JSONObject object = new JSONObject(context.getOptionsData().toMap());
        object.put("page", indexPageVars);
        object.put("theme", context.getTheme());

        String notFoundIndexContent;
        if (resource != null) {
            notFoundIndexContent = context.compile(resource.getReference().getExtension(), resource.getContent(), object.toMap());
        }
        else {
            notFoundIndexContent = object.toString(2);
        }

        OrchidPage page = new OrchidPage(new StringResource(context, "404.txt", notFoundIndexContent), "404");
        page.addJs("assets/js/shadowComponents.js");
        for (String template : strategy.getPageLayout(page)) {
            OrchidResource templateResource = context.getResourceEntry(template);
            if (templateResource != null) {
                content = "" + context.compile(FilenameUtils.getExtension(template), templateResource.getContent(), page);
            }
        }

        return NanoHTTPD.newFixedLengthResponse(content);
    }
}
