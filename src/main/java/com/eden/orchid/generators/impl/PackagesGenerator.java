package com.eden.orchid.generators.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.AutoRegister;
import com.eden.orchid.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.OrchidUtils;
import com.eden.orchid.docParser.PackageDocParser;
import com.eden.orchid.generators.Generator;
import com.eden.orchid.resources.OrchidEntry;
import com.eden.orchid.resources.OrchidResources;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
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
    public void startGeneration(RootDoc root) {
        OrchidEntry packageDocTemplate = OrchidResources.getResourceEntry("templates/containers/classDoc.html");

        String containerName = "packageDoc.html";
        String layoutName = "index.html";

        if(packageDocTemplate != null) {
            JSONElement layoutElement = packageDocTemplate.queryEmbeddedData("layout");
            if(layoutElement != null) {
                layoutName = layoutElement.toString();
            }
        }

        Clog.i("Package files will be compiled with '#{$1}' layout", new Object[]{layoutName});

        Set<String> packageNamesSet = new TreeSet<>();
        for (ClassDoc classDoc : root.classes()) {
            packageNamesSet.add(classDoc.containingPackage().name());
        }

        for (String packageName : packageNamesSet) {
            PackageDoc packageDoc = root.packageNamed(packageName);

            JSONObject packageInfoJson = PackageDocParser.createPackageDocJson(packageDoc);
            JSONObject packageHeadJson = PackageDocParser.getPackageHeadInfo(packageDoc);

            JSONObject object = new JSONObject(Orchid.getRoot().toMap());
            object.put("packageDoc", packageInfoJson);
            object.put("head", packageHeadJson);
            object.put("root", object.toMap());

            object.put("content", OrchidUtils.compileContainer(containerName, object));

            String finalCompiledContent = OrchidUtils.compileLayout(layoutName, object);

            OrchidResources.writeFile("classes/" + packageInfoJson.getString("name").replaceAll("\\.", File.separator), "index.html", finalCompiledContent);
        }
    }
}
