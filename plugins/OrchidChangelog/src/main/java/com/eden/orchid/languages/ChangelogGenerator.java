package com.eden.orchid.languages;


import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.OrchidParser;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ChangelogGenerator extends OrchidGenerator {

    protected OrchidResources resources;

    @Inject
    public ChangelogGenerator(OrchidContext context, OrchidResources resources) {
        super(context);
        this.priority = 700;
        this.resources = resources;
    }

    @Override
    public String getKey() {
        return "changelog";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        List<ChangelogPage> pages = new ArrayList<>();
        JSONObject changelog = resources.getLocalDatafile("changelog");

        if(changelog != null) {
            if(changelog.has(OrchidParser.arrayAsObjectKey)) {
                JSONArray versions = changelog.getJSONArray(OrchidParser.arrayAsObjectKey);

                for (int i = 0; i < versions.length(); i++) {
                    pages.add(new ChangelogPage(context, versions.getJSONObject(i), versions.getJSONObject(i).getString("version")));
                }
            }
            else {
                for (String key : changelog.keySet()) {
                    JSONObject version = changelog.getJSONObject(key);
                    version.put("version", key);
                    pages.add(new ChangelogPage(context, version, key));
                }
            }
        }

        return pages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.stream().forEach(OrchidPage::renderTemplate);
    }
}