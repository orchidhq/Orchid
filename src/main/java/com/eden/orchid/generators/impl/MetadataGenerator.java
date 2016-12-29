package com.eden.orchid.generators.impl;

import com.eden.orchid.AutoRegister;
import com.eden.orchid.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.generators.Generator;
import com.sun.javadoc.RootDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public JSONElement startIndexing(RootDoc root) {
        JSONArray metadataFiles = new JSONArray();

        String baseUrl = "";

        if(Orchid.query("options.baseUrl") != null) {
            baseUrl = Orchid.query("options.baseUrl").toString();
        }

        JSONObject classIndex = new JSONObject();
        classIndex.put("simpleName", "classIndex");
        classIndex.put("name", "classIndex");
        classIndex.put("url", baseUrl + File.separator + "/meta/classIndex.json");
        metadataFiles.put(classIndex);

        return new JSONElement(metadataFiles);
    }

    @Override
    public JSONElement startGeneration(RootDoc root) {
        JSONArray metadataFiles = new JSONArray();

        try {
            String outputPath = Orchid.query("options.d").getElement().toString()
                    + File.separator + "meta";

            File outputFile = new File(outputPath);
            Path classesFile = Paths.get(outputPath + File.separator + "classIndex.json");
            if (!outputFile.exists()) {
                outputFile.mkdirs();
            }

            String compiledContent = ((JSONArray) Orchid.query("index.classes.internal").getElement()).toString(2);

            Files.write(classesFile, compiledContent.getBytes());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONElement(metadataFiles);
    }
}
