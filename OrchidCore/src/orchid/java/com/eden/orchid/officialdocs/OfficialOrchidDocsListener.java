package com.eden.orchid.officialdocs;

import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.On;
import com.eden.orchid.api.events.OrchidEventListener;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.pages.PagesGenerator;
import com.eden.orchid.pages.pages.StaticPage;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfficialOrchidDocsListener implements OrchidEventListener {

    private final OrchidContext context;

    @Inject
    public OfficialOrchidDocsListener(OrchidContext context) {
        this.context = context;
    }

    @On(Orchid.Lifecycle.IndexingFinish.class)
    public void onIndexingFinish(Orchid.Lifecycle.IndexingFinish ev) {
        List<OrchidPage> staticPages = context.getGeneratorPages(PagesGenerator.GENERATOR_KEY);

        final Map<String, List<OrchidPage>> pluginDocsMap = new HashMap<>();
        final Map<String, OrchidPage> pluginRootMap = new HashMap<>();

        staticPages.forEach(page -> {
            OrchidReference referenceCopy = new OrchidReference(page.getReference());
            if(referenceCopy.getPathSegments().length > 1) {
                String pluginName = referenceCopy.getPathSegment(1);
                pluginDocsMap.computeIfAbsent(pluginName, s -> new ArrayList<>());


                if(referenceCopy.getPathSegments().length >= 3 && referenceCopy.getPathSegment(2).equals("docs")) {
                    pluginDocsMap.get(pluginName).add(page);
                }
                else if(referenceCopy.getPathSegments().length == 2) {
                    pluginRootMap.put(pluginName, page);
                }
            }
        });

        staticPages.forEach(page -> {
            OrchidReference referenceCopy = new OrchidReference(page.getReference());
            if(referenceCopy.getPathSegments().length > 1) {
                String pluginName = referenceCopy.getPathSegment(1);

                List<OrchidPage> pluginDocsPages = pluginDocsMap.get(pluginName);
                pluginDocsPages.sort((o1, o2) -> {
                    int orderValue = 0;
                    if(o1.get("order") != null && o2.get("order") != null) {
                        orderValue = Integer.parseInt(o1.get("order").toString()) - Integer.parseInt(o2.get("order").toString());
                    }

                    if(orderValue == 0) {
                        orderValue = o1.getTitle().compareTo(o2.getTitle());
                    }

                    return orderValue;
                });

                JSONArray menuItemsJson = new JSONArray();

                JSONObject rootPluginMenuItemJson = new JSONObject();
                rootPluginMenuItemJson.put("type", "page");
                rootPluginMenuItemJson.put("title", "Home");
                rootPluginMenuItemJson.put("itemId", pluginRootMap.get(pluginName).getTitle());
                rootPluginMenuItemJson.put("collectionType", PagesGenerator.GENERATOR_KEY);
                rootPluginMenuItemJson.put("collectionId", ((StaticPage) pluginRootMap.get(pluginName)).getGroup());
                menuItemsJson.put(rootPluginMenuItemJson);

                for(OrchidPage docPage : pluginDocsPages) {
                    JSONObject menuItemJson = new JSONObject();
                    menuItemJson.put("type", "page");
                    menuItemJson.put("itemId", docPage.getTitle());
                    menuItemJson.put("collectionType", PagesGenerator.GENERATOR_KEY);
                    menuItemJson.put("collectionId", ((StaticPage) docPage).getGroup());

                    if(docPage.getReference().getPathSegments().length == 3 && docPage.getReference().getPathSegment(2).equals("docs")) {
                        if(((StaticPage) docPage).getGroup().equals("themes")) {
                            menuItemJson.put("title", "Demo");
                        }
                        else {
                            menuItemJson.put("title", "Documentation");
                        }
                        menuItemsJson.put(1, menuItemJson);
                    }
                    else {
                        menuItemsJson.put(menuItemJson);
                    }
                }

                page.getMenu().add(menuItemsJson);
            }
        });

    }
}
