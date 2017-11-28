package com.eden.orchid.presentations;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Description("Indexes form definitions so they can be easily referenced from components on different pages.")
public class FormsGenerator extends OrchidGenerator {

    private final FormsModel model;

    @Option("baseDir")
    @StringDefault("forms")
    public String formsBaseDir;

    @Inject
    public FormsGenerator(OrchidContext context, FormsModel model) {
        super(context, "forms", 20);
        this.model = model;
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        Map<String, Form> forms = new HashMap<>();

        JSONObject formsData = context.getDatafiles(OrchidUtils.normalizePath(formsBaseDir));

        for(String key : formsData.keySet()) {
            forms.put(key, new Form(context, key, formsData.getJSONObject(key)));
        }

        model.initialize(forms);

        return null;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {

    }

}
