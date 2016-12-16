package com.eden.orchid.discover;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;
import java.util.TreeSet;

public class DiscoverPackages {
    public static JSONArray startDiscovery(RootDoc root) {
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
