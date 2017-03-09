package com.eden.orchid.impl.generators;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.OrchidReference;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.JsonResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;

public class IndexGenerator extends OrchidGenerator {

    private OrchidResources resources;

    @Inject
    public IndexGenerator(OrchidContext context, OrchidResources resources) {
        super(context);
        this.resources = resources;

        setPriority(1);
    }

    @Override
    public String getName() {
        return "indices";
    }

    @Override
    public String getDescription() {
        return "Generates index files to connect your site to others.";
    }

    @Override
    public List<OrchidPage> startIndexing() {
        return null;
    }

    @Override
    public void startGeneration(List<OrchidPage> pages) {
        generateIndexFiles();
    }

    private void generateIndexFiles() {
        JSONObject index = (JSONObject) context.query("index").getElement();

        JSONObject flatIndex = new JSONObject();

        for (String key : index.keySet()) {
            JSONObject indexPart = index.getJSONObject(key);
            JSONArray flatIndexPart = new JSONArray();

            List items = OrchidUtils.walkObject(indexPart, "url");

            for(Object item : items) {
                flatIndexPart.put(item);
            }

            flatIndex.put(key, flatIndexPart);

            OrchidResource resource = new JsonResource(new JSONElement(flatIndexPart), new OrchidReference(context, "meta/" + key + ".index.json"));
            new OrchidPage(resource).renderRaw();
        }

        OrchidResource resource = new JsonResource(new JSONElement(flatIndex), new OrchidReference(context, "meta/index.json"));
        new OrchidPage(resource).renderRaw();
    }
}
