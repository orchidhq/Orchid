package com.eden.orchid.impl.generators;

import com.eden.common.json.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.generators.Generator;
import com.eden.orchid.generators.docParser.ClassDocParser;
import com.eden.orchid.resources.OrchidPage;
import com.eden.orchid.resources.OrchidReference;
import com.eden.orchid.impl.resources.StringResource;
import com.eden.orchid.utilities.AutoRegister;
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
        return 90;
    }

    @Override
    public JSONElement startIndexing() {
        RootDoc root = Orchid.getRootDoc();

        if(root == null) {
            return null;
        }

        JSONObject classIndex = new JSONObject();
        classIndex.put("internal", new JSONArray());
        classIndex.put("external", new JSONArray());

        for(ClassDoc classDoc : root.classes()) {
            JSONObject item = new JSONObject();

            JSONObject classInfoJson = ClassDocParser.createClassDocJson(classDoc);

            item.put("info", classInfoJson.getJSONObject("info"));

            OrchidReference classReference = new OrchidReference("classes", classDoc.qualifiedTypeName().replaceAll("\\.", File.separator) + File.separator + "index.html");
            classReference.setTitle(classDoc.name());

            // add basic Class indexing info
            item.put("simpleName", classReference.getTitle());
            item.put("name", classReference.getTitle());
            item.put("url", classReference.toString());

            classIndex.getJSONArray("internal").put(item);
        }

        if(Orchid.query("options.data.additionalClasses") != null) {
            applyAdditionalClasses(classIndex.getJSONArray("external"));
        }

        return new JSONElement(classIndex);
    }

    @Override
    public void startGeneration() {
        RootDoc root = Orchid.getRootDoc();

        if(root == null) {
            return;
        }

        for(ClassDoc classDoc : root.classes()) {
            JSONObject classInfoJson = ClassDocParser.createClassDocJson(classDoc);

            OrchidReference packageReference = new OrchidReference("classes", classDoc.qualifiedTypeName().replaceAll("\\.", File.separator) + File.separator + "index.html");
            packageReference.setTitle(classDoc.name());

            OrchidPage classPage = new OrchidPage(new StringResource("", packageReference));
            classPage.setData(classInfoJson);
            classPage.setAlias("classDoc");

            classPage.renderTemplate("templates/pages/classDoc.twig");
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
