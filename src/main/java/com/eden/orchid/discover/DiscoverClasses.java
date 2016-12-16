package com.eden.orchid.discover;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import org.json.JSONArray;
import org.json.JSONObject;

public class DiscoverClasses {
    public static JSONArray startDiscovery(RootDoc root) {
        JSONArray jsonArray = new JSONArray();

        for(ClassDoc classDoc : root.classes()) {
            JSONObject classInfoJson = new JSONObject();
            classInfoJson.put("simpleName", classDoc.name());
            classInfoJson.put("name", classDoc.qualifiedName());
            classInfoJson.put("package", classDoc.containingPackage().name());

            jsonArray.put(classInfoJson);
        }

        return jsonArray;
    }
}
