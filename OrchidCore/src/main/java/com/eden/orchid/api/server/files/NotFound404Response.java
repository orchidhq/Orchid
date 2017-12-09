package com.eden.orchid.api.server.files;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.server.OrchidResponse;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.InputStream;
import java.nio.charset.Charset;

public final class NotFound404Response {

    private final OrchidContext context;

    @Inject
    public NotFound404Response(OrchidContext context) {
        this.context = context;
    }

    public OrchidResponse getResponse(String targetPath) {
        String content = "";
        Clog.i("Rendering 404: #{$1}", targetPath);

        OrchidResource resource = context.getResourceEntry("templates/server/404.peb");

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
        InputStream is = context.getRenderedTemplate(page);
        try {
            content = IOUtils.toString(is, Charset.defaultCharset());
        }
        catch (Exception e) {
            content = "";
        }

        return new OrchidResponse(context).content(content);
    }
}
