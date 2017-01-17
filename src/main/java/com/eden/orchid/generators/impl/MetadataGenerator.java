package com.eden.orchid.generators.impl;

import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.utilities.OrchidUtils;
import com.eden.orchid.generators.Generator;
import com.eden.orchid.resources.OrchidResources;
import org.json.JSONArray;
import org.json.JSONObject;

@AutoRegister
public class MetadataGenerator implements Generator {

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public String getName() {
        return "meta";
    }

    @Override
    public JSONElement startIndexing() {
        JSONObject metadataFiles = new JSONObject();

        JSONObject classIndex = new JSONObject();
        classIndex.put("simpleName", "classIndex");
        classIndex.put("name", "classIndex");
        classIndex.put("url", OrchidUtils.applyBaseUrl("/meta/classIndex.json"));
        metadataFiles.put("classIndex", classIndex);

        return new JSONElement(metadataFiles);
    }

    @Override
    public void startGeneration() {
        String compiledContent = ((JSONArray) Orchid.query("index.classes.internal").getElement()).toString(2);
        OrchidResources.writeFile("meta", "classIndex.json", compiledContent);
    }
}
