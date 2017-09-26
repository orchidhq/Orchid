package com.eden.orchid.languages;


import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.theme.pages.OrchidExternalPage;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ChangelogGenerator extends OrchidGenerator {

    @Inject
    public ChangelogGenerator(OrchidContext context) {
        super(context, "changelog", 700);
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        List<OrchidPage> pages = new ArrayList<>();
        JSONObject changelog = context.getDatafile("changelog");

        if(changelog != null) {
            if(changelog.has(OrchidParser.arrayAsObjectKey)) {
                JSONArray versions = changelog.getJSONArray(OrchidParser.arrayAsObjectKey);

                for (int i = 0; i < versions.length(); i++) {
                    pages.add(new OrchidExternalPage(OrchidReference.fromUrl(
                            context,
                            versions.getJSONObject(i).getString("version"),
                            versions.getJSONObject(i).getString("url"))));
                }
            }
            else {
                for (String key : changelog.keySet()) {
                    pages.add(new OrchidExternalPage(OrchidReference.fromUrl(
                            context,
                            key,
                            changelog.getJSONObject(key).getString("url"))));
                }
            }
        }

        return pages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {

    }
}