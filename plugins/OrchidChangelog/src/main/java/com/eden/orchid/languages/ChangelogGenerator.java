package com.eden.orchid.languages;


import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.ListClass;
import com.eden.orchid.api.resources.resource.JsonResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.languages.model.ChangelogModel;
import com.eden.orchid.languages.model.ChangelogVersion;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Singleton
public class ChangelogGenerator extends OrchidGenerator {

    private final ChangelogModel model;

    @Getter @Setter
    @Option
    private String sourceUrl;

    @Getter @Setter
    @Option @ListClass(ChangelogVersion.class)
    private List<ChangelogVersion> versions;

    @Inject
    public ChangelogGenerator(OrchidContext context, ChangelogModel model) {
        super(context, "changelog", 700);
        this.model = model;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        OrchidPage page = null;
        if(!EdenUtils.isEmpty(versions)) {
            versions.sort(Comparator.comparing(ChangelogVersion::getVersion).reversed());

            model.initialize(versions, sourceUrl);

            JSONArray array = new JSONArray();
            for(ChangelogVersion version : versions) {
                array.put(new JSONObject(version));
            }

            JSONElement versionData = new JSONElement(array);
            OrchidReference reference = new OrchidReference(context, "versions.json");
            reference.setUsePrettyUrl(false);

            page = new OrchidPage(new JsonResource(versionData, reference), "versions");
        }
        else {
            model.initialize(new ArrayList<>(), null);
        }

        List<OrchidPage> pages = new ArrayList<>();
        if(page != null) {
            pages.add(page);
        }

        return pages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.forEach(context::renderRaw);
    }
}