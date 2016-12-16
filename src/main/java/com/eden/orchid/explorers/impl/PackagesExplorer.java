package com.eden.orchid.explorers.impl;

import com.eden.orchid.AutoRegister;
import com.eden.orchid.explorers.DocumentationExplorer;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;
import java.util.TreeSet;

@AutoRegister
public class PackagesExplorer implements DocumentationExplorer {

    @Override
    public String getKey() {
        return "packages";
    }

    public JSONArray startDiscovery(RootDoc root) {
        JSONArray jsonArray = new JSONArray();

        Set<String> packageNamesSet = new TreeSet<>();

        for(ClassDoc classDoc : root.classes()) {
            packageNamesSet.add(classDoc.containingPackage().name());
        }

        for(String packageName : packageNamesSet) {
            PackageDoc packageDoc = root.packageNamed(packageName);

            JSONObject packageInfoJson = new JSONObject();
            packageInfoJson.put("name", packageDoc.name());

            jsonArray.put(packageInfoJson);
        }

        return jsonArray;
    }
}
