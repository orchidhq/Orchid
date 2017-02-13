package com.eden.orchid.impl.generators;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;
import org.json.JSONObject;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MetadataGenerator extends OrchidGenerator {

    private List<OrchidPage> meta;

    public MetadataGenerator() {
        this.priority = 100;
    }

    @Override
    public String getName() {
        return "meta";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public JSONElement startIndexing() {
        JSONObject siteAssets = new JSONObject();
        meta = new ArrayList<>();

        siteAssets.put("meta", new JSONObject());

//        if(Orchid.getRootDoc() != null) {
//            JSONElement el = Orchid.query("index.classes.internal");
//
//            if (el != null) {
//                OrchidResource resource = new JsonResource(el, new OrchidReference("meta/classIndex.json"));
//                OrchidPage entry = new OrchidPage(resource);
//
//                JSONObject index = new JSONObject();
//                index.put("name", entry.getReference().getTitle());
//                index.put("title", entry.getReference().getTitle());
//                index.put("url", entry.getReference().toString());
//
//                meta.add(entry);
//
//                OrchidUtils.buildTaxonomy(resource, siteAssets, index);
//            }
//        }

//        if(Orchid.getRootDoc() != null) {
//            JSONElement el = Orchid.query("index.externalClasses");
//
//            if (el != null) {
//                OrchidResource resource = new JsonResource(el, new OrchidReference("meta/externalClasses.json"));
//                OrchidPage entry = new OrchidPage(resource);
//
//                JSONObject index = new JSONObject();
//                index.put("name", entry.getReference().getTitle());
//                index.put("title", entry.getReference().getTitle());
//                index.put("url", entry.getReference().toString());
//
//                meta.add(entry);
//
//                OrchidUtils.buildTaxonomy(resource, siteAssets, index);
//            }
//        }

        return new JSONElement(siteAssets.getJSONObject("meta"));
    }

    @Override
    public void startGeneration() {
        for (OrchidPage metaPage : meta) {
            metaPage.renderRawContent();
        }
    }
}
