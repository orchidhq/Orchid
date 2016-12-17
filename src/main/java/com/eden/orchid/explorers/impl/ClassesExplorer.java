package com.eden.orchid.explorers.impl;

import com.eden.orchid.AutoRegister;
import com.eden.orchid.explorers.DocumentationExplorer;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
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

            classInfoJson.put("methods", new JSONArray());

            for(MethodDoc methodDoc : classDoc.methods()) {
                JSONObject methodDocJson = new JSONObject();

                String visibility = "";
                if(methodDoc.isPublic())              visibility = "public";
                else if(methodDoc.isProtected())      visibility = "protected";
                else if(methodDoc.isPackagePrivate()) visibility = "package-private";
                else if(methodDoc.isPrivate())        visibility = "private";

                methodDocJson.put("visibility", visibility);
                methodDocJson.put("static", methodDoc.isStatic());
                methodDocJson.put("abstract", methodDoc.isAbstract());
                methodDocJson.put("synchronized", methodDoc.isSynchronized());
                methodDocJson.put("final", methodDoc.isFinal());
                methodDocJson.put("returns", new JSONObject());

                methodDocJson.getJSONObject("returns").put("simpleName", methodDoc.returnType().simpleTypeName());
                methodDocJson.getJSONObject("returns").put("name", methodDoc.returnType().qualifiedTypeName());
                methodDocJson.getJSONObject("returns").put("dimension", methodDoc.returnType().dimension());

                methodDocJson.put("name", methodDoc.name());


                methodDocJson.put("parameters", new JSONArray());

                for(Parameter parameter : methodDoc.parameters()) {
                    JSONObject parameterJson = new JSONObject();

                    parameterJson.put("simpleName", parameter.type().simpleTypeName());
                    parameterJson.put("name", parameter.type().qualifiedTypeName());
                    parameterJson.put("dimension", parameter.type().dimension());
                    parameterJson.put("variableName", parameter.typeName());

                    methodDocJson.getJSONArray("parameters").put(parameterJson);
                }

                classInfoJson.getJSONArray("methods").put(methodDocJson);
            }






















            jsonArray.put(classInfoJson);
        }

        return jsonArray;
    }
}
