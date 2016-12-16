package com.eden.orchid.explorers.impl;

import com.eden.orchid.AutoRegister;
import com.eden.orchid.explorers.DocumentationExplorer;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import org.json.JSONArray;
import org.json.JSONObject;

@AutoRegister
public class ClassesExplorer implements DocumentationExplorer {

    @Override
    public String getKey() {
        return "classes";
    }

    public JSONArray startDiscovery(RootDoc root) {
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
