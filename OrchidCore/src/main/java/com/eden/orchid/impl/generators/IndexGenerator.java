package com.eden.orchid.impl.generators;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.indexing.OrchidInternalIndex;
import com.eden.orchid.api.indexing.OrchidRootInternalIndex;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.resources.resource.JsonResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Description("Generates index files to connect your site to others.")
public final class IndexGenerator extends OrchidGenerator {

    @Inject
    public IndexGenerator(OrchidContext context) {
        super(context, "indices", 1);
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        return null;
    }

    @Override
    public void startGeneration(Stream<? extends OrchidPage> pages) {
        generateIndexFiles();
    }

    private void generateIndexFiles() {
        OrchidRootInternalIndex internalIndex = context.getInternalIndex();
        Map<String, OrchidInternalIndex> mappedIndex = internalIndex.getAllIndexedPages();

        OrchidIndex indices = new OrchidInternalIndex("index");

        // Render an page for each generator's individual index
        mappedIndex.keySet().forEach(key -> {
            JSONElement jsonElement = new JSONElement(mappedIndex.get(key).toJSON(true, false));
            OrchidReference reference = new OrchidReference(context, "meta/" + key + ".index.json");
            OrchidResource resource = new JsonResource(jsonElement, reference);
            OrchidPage page = new OrchidPage(resource, "index");
            page.getReference().setUsePrettyUrl(false);
            context.renderRaw(page);

            indices.addToIndex(indices.getOwnKey() + "/" + page.getReference().getPath(), page);
        });

        // Render full composite index page
        JSONElement compositeJsonElement = new JSONElement(internalIndex.toJSON(true, false));
        OrchidReference compositeReference = new OrchidReference(context, "meta/all.index.json");
        OrchidResource compositeIndexResource = new JsonResource(compositeJsonElement, compositeReference);
        OrchidPage compositeIndexPage = new OrchidPage(compositeIndexResource, "index");
        compositeIndexPage.getReference().setUsePrettyUrl(false);
        context.renderRaw(compositeIndexPage);
        indices.addToIndex(indices.getOwnKey() + "/" + compositeIndexPage.getReference().getPath(), compositeIndexPage);

        // Render an index of all indices, so individual index pages can be found
        for(OrchidPage page : indices.getAllPages()) {
            page.setData(new JSONObject());
        }
        OrchidResource indexResource = new JsonResource(new JSONElement(indices.toJSON(false, false)), new OrchidReference(context, "meta/index.json"));
        OrchidPage indicesPage = new OrchidPage(indexResource, "index");
        indicesPage.getReference().setUsePrettyUrl(false);
        context.renderRaw(indicesPage);
    }
}
