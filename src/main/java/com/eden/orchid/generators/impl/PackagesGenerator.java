package com.eden.orchid.generators.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.AutoRegister;
import com.eden.orchid.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.OrchidUtils;
import com.eden.orchid.docParser.PackageDocParser;
import com.eden.orchid.generators.Generator;
import com.eden.orchid.utilities.OrchidPair;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

@AutoRegister
public class PackagesGenerator implements Generator {

    @Override
    public String getName() {
        return "packages";
    }

    @Override
    public int priority() {
        return 90;
    }

    @Override
    public JSONElement startIndexing(RootDoc root) {

        JSONArray packageIndex = new JSONArray();

        Set<String> packageNamesSet = new TreeSet<>();

        String baseUrl = "";

        if(Orchid.query("options.baseUrl") != null) {
            baseUrl = Orchid.query("options.baseUrl").toString();
        }

        for(ClassDoc classDoc : root.classes()) {
            packageNamesSet.add(classDoc.containingPackage().name());
        }

        for(String packageName : packageNamesSet) {
            PackageDoc packageDoc = root.packageNamed(packageName);

            JSONObject item = new JSONObject();
            item.put("name", packageDoc.name());
            item.put("url", baseUrl + File.separator + "classes/" + packageDoc.name().replaceAll("\\.", File.separator));
            packageIndex.put(item);
        }

        return new JSONElement(packageIndex);
    }

    @Override
    public JSONElement startGeneration(RootDoc root) {
        JSONArray jsonArray = new JSONArray();

        String packageDocTemplate = OrchidUtils.getResourceFileContents("templates/containers/packageDoc.html");
        String layout;

        OrchidPair<String, JSONElement> packageDocEmbeddedData = Orchid.getTheme().getEmbeddedData(packageDocTemplate);

        if((packageDocEmbeddedData != null) && (packageDocEmbeddedData.second.getElement() instanceof JSONObject) && (((JSONObject) packageDocEmbeddedData.second.getElement()).has("layout"))) {
            layout = OrchidUtils.getResourceFileContents("templates/layouts/" + ((JSONObject) packageDocEmbeddedData.second.getElement()).getString("layout"));
            packageDocTemplate = packageDocEmbeddedData.first;

            Clog.i("Class files will be compiled with '#{$1}' layout", new Object[]{ ((JSONObject) packageDocEmbeddedData.second.getElement()).getString("layout") });
        }
        else {
            layout = OrchidUtils.getResourceFileContents("templates/layouts/index.html");
            Clog.d("Package files should be compiled with basic index layout");
        }
        JSONObject rootData = new JSONObject(Orchid.getRoot().toMap());

        Set<String> packageNamesSet = new TreeSet<>();
        for(ClassDoc classDoc : root.classes()) {
            packageNamesSet.add(classDoc.containingPackage().name());
        }

        for(String packageName : packageNamesSet) {
            PackageDoc packageDoc = root.packageNamed(packageName);

            JSONObject packageInfoJson = PackageDocParser.createPackageDocJson(packageDoc);

            JSONObject object = new JSONObject(Orchid.getRoot().toMap());
            object.put("root", Orchid.getRoot().toMap());
            object.put("packageDoc", packageInfoJson);
            object.getJSONObject("root").put("packageDoc", packageInfoJson);

            try {
                String outputPath = Orchid.query("options.d").getElement().toString()
                        + File.separator + "classes"
                        + File.separator + packageInfoJson.getString("name").replaceAll("\\.", File.separator);

                File outputFile = new File(outputPath);
                Path classesFile = Paths.get(outputPath + File.separator + "index.html");
                if (!outputFile.exists()) {
                    outputFile.mkdirs();
                }

                String compiledContent = Orchid.getTheme().compile("html", packageDocTemplate, object);

                compiledContent = ClassesGenerator.applyLayout(layout, rootData, compiledContent);

                Files.write(classesFile, compiledContent.getBytes());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new JSONElement(jsonArray);
    }
}
