package com.eden.orchid.generators.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.AutoRegister;
import com.eden.orchid.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.OrchidResources;
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
    public void startGeneration(RootDoc root) {
        OrchidPair<String, JSONElement> classDocTemplate = OrchidResources.readResource("templates/containers/classDoc.html");

        String containerName = "classDoc.html";
        String layoutName = "index.html";

        if((classDocTemplate.second.getElement() instanceof JSONObject) && (((JSONObject) classDocTemplate.second.getElement()).has("layout"))) {
            layoutName = ((JSONObject) classDocTemplate.second.getElement()).getString("layout");
        }

        Clog.i("Class files will be compiled with '#{$1}' layout", new Object[]{layoutName});

        for(ClassDoc classDoc : root.classes()) {
            JSONObject classInfoJson = ClassDocParser.createClassDocJson(classDoc);
            JSONObject classHeadJson = ClassDocParser.getClassHeadInfo(classDoc);

            JSONObject object = new JSONObject(Orchid.getRoot().toMap());
            object.put("classDoc", classInfoJson);
            object.put("head", classHeadJson);
            object.put("root", object.toMap());

            object.put("content", OrchidUtils.compileContainer(containerName, object));

            String finalCompiledContent = OrchidUtils.compileLayout(layoutName, object);

            OrchidResources.writeFile("classes/" + classInfoJson.getJSONObject("info").getString("name").replaceAll("\\.", File.separator), "index.html", finalCompiledContent);
        }
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
