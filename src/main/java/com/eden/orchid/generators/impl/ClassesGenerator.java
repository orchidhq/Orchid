package com.eden.orchid.generators.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.AutoRegister;
import com.eden.orchid.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.OrchidUtils;
import com.eden.orchid.docParser.ClassDocParser;
import com.eden.orchid.generators.Generator;
import com.eden.orchid.utilities.OrchidPair;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@AutoRegister
public class ClassesGenerator implements Generator {

    private static OkHttpClient client = new OkHttpClient();

    @Override
    public String getName() {
        return "classes";
    }

    @Override
    public int priority() {
        return 80;
    }

    @Override
    public JSONElement startIndexing(RootDoc root) {
        JSONObject classIndex = new JSONObject();
        classIndex.put("internal", new JSONArray());
        classIndex.put("external", new JSONArray());

        String baseUrl = "";

        if(Orchid.query("options.baseUrl") != null) {
            baseUrl = Orchid.query("options.baseUrl").toString();
        }

        for(ClassDoc classDoc : root.classes()) {
            JSONObject item = new JSONObject();

            // add basic Class indexing info
            item.put("simpleName", classDoc.name());
            item.put("name", classDoc.qualifiedName());
            item.put("url", baseUrl + File.separator + "classes/" + classDoc.qualifiedName().replaceAll("\\.", File.separator));

            // add Class superclass indexing info
            if(classDoc.superclass() != null) {
                JSONObject superClassType = new JSONObject();
                superClassType.put("simpleName", classDoc.superclass().name());
                superClassType.put("name", classDoc.superclass().qualifiedName());

                item.put("superclass", superClassType);
            }

            // add Class superclass interfaces info
            if(classDoc.interfaces().length > 0) {
                JSONArray interfaces = new JSONArray();

                for(ClassDoc type : classDoc.interfaces()) {
                    JSONObject interfaceType = new JSONObject();
                    interfaceType.put("simpleName", type.name());
                    interfaceType.put("name", type.qualifiedName());

                    interfaces.put(interfaceType);
                }

                item.put("interfaces", interfaces);
            }

            classIndex.getJSONArray("internal").put(item);
        }

        if(Orchid.query("options.data.additionalClasses") != null) {
            applyAdditionalClasses(classIndex.getJSONArray("external"));
        }

        return new JSONElement(classIndex);
    }

    @Override
    public JSONElement startGeneration(RootDoc root) {
        JSONArray jsonArray = new JSONArray();
        String classDocTemplate = OrchidUtils.getResourceFileContents("templates/containers/classDoc.html");

        OrchidPair<String, JSONElement> classDocEmbeddedData = Orchid.getTheme().getEmbeddedData(classDocTemplate);

        String layout;

        if((classDocEmbeddedData != null) && (classDocEmbeddedData.second.getElement() instanceof JSONObject) && (((JSONObject) classDocEmbeddedData.second.getElement()).has("layout"))) {
            layout = OrchidUtils.getResourceFileContents("templates/layouts/" + ((JSONObject) classDocEmbeddedData.second.getElement()).getString("layout"));
            classDocTemplate = classDocEmbeddedData.first;
        }
        else {
            layout = OrchidUtils.getResourceFileContents("templates/layouts/index.html");
            Clog.d("Class files should be compiled with basic index layout");
        }

        for(ClassDoc classDoc : root.classes()) {
             JSONObject classInfoJson = ClassDocParser.createClassDocJson(classDoc);
             JSONObject classHeadJson = ClassDocParser.getClassHeadInfo(classDoc);

            JSONObject object = new JSONObject(Orchid.getRoot().toMap());
            object.put("root", Orchid.getRoot().toMap());
            object.put("classDoc", classInfoJson);
            object.put("head", classHeadJson);
            object.getJSONObject("root").put("classDoc", classInfoJson);
            object.getJSONObject("root").put("head", classHeadJson);

            jsonArray.put(classInfoJson);

            try {
                String outputPath = Orchid.query("options.d").getElement().toString()
                        + File.separator + "classes"
                        + File.separator + classInfoJson.getJSONObject("info").getString("name").replaceAll("\\.", File.separator);

                File outputFile = new File(outputPath);
                Path classesFile = Paths.get(outputPath + File.separator + "index.html");
                if (!outputFile.exists()) {
                    outputFile.mkdirs();
                }

                String compiledContent = Orchid.getTheme().compile("html", classDocTemplate, object);

                compiledContent = applyLayout(layout, object, compiledContent);

                Files.write(classesFile, compiledContent.getBytes());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new JSONElement(jsonArray);
    }

    public static String applyLayout(String layout, JSONObject data, String content) {
        JSONObject object = new JSONObject(data.toMap());

        object.put("content", content);

        return Orchid.getTheme().compile("html", layout, object);
    }

    private static void applyAdditionalClasses(JSONArray classIndex) {
        Object additionalClasses = Orchid.query("options.data.additionalClasses").getElement();

        if(additionalClasses instanceof JSONObject) {
            JSONObject additionalClassesObject = (JSONObject) additionalClasses;

            // If classes are declared in the additionalClasses.yml, add them here
            if(additionalClassesObject.has("classes")) {
                JSONArray classesArray = additionalClassesObject.getJSONArray("classes");
                for(int i = 0; i < classesArray.length(); i++) {
                    classIndex.put(classesArray.getJSONObject(i));
                }
            }

            if(additionalClassesObject.has("links")) {
                JSONArray classesArray = additionalClassesObject.getJSONArray("links");

                for(int i = 0; i < classesArray.length(); i++) {
                    loadAdditionalFile(classIndex, classesArray.getString(i));
                }
            }
        }
        else if(additionalClasses instanceof JSONArray) {
            for(int i = 0; i < ((JSONArray) additionalClasses).length(); i++) {
                loadAdditionalFile(classIndex, ((JSONArray) additionalClasses).getString(i));
            }
        }
    }

    private static void loadAdditionalFile(JSONArray classIndex, String url) {
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();

            if(response.isSuccessful()) {
                JSONArray responseArray = new JSONArray(response.body().string());
                for(int i = 0; i < responseArray.length(); i++) {
                    classIndex.put(responseArray.getJSONObject(i));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
