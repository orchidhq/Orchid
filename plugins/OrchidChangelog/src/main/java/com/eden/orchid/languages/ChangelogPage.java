package com.eden.orchid.languages;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.JsonResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import org.json.JSONObject;

public class ChangelogPage extends OrchidPage {

    public ChangelogPage(OrchidContext context, JSONObject changelogData, String versionName) {
        this(new JsonResource(new JSONElement(changelogData), new OrchidReference(context, "versions/" + versionName + ".html")));
    }

    public ChangelogPage(OrchidResource resource) {
        super(resource, "changelog");
    }
}
