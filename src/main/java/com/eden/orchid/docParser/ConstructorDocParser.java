package com.eden.orchid.docParser;

import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConstructorDocParser {
    public static JSONObject getConstructorObject(ConstructorDoc constructorDoc) {
        JSONObject constructorDocJson = new JSONObject();
        constructorDocJson.put("modifiers", constructorDoc.modifiers());

        constructorDocJson.put("name", constructorDoc.name());

        if(constructorDoc.parameters().length > 0) {
            constructorDocJson.put("parameters", new JSONArray());

            for(Parameter parameter : constructorDoc.parameters()) {
                JSONObject parameterObject = ParserUtils.getTypeObject(parameter.type());
                parameterObject.put("variableName", parameter.name());

                constructorDocJson.getJSONArray("parameters").put(parameterObject);
            }

            if(constructorDoc.isVarArgs()) {
                constructorDocJson.getJSONArray("parameters").getJSONObject(constructorDocJson.getJSONArray("parameters").length() - 1).put("dimension", "...");
            }
        }

        if(constructorDoc.commentText().length() > 0) {
            constructorDocJson.put("comment", ParserUtils.getCommentDescription(constructorDoc));

            for(ParamTag paramTag : constructorDoc.paramTags()) {
                for(int i = 0; i < constructorDocJson.getJSONArray("parameters").length(); i++) {
                    JSONObject parameterObject = constructorDocJson.getJSONArray("parameters").getJSONObject(i);

                    if(parameterObject.getString("variableName").equals(paramTag.parameterName())) {
                        parameterObject.put("description", paramTag.parameterComment());
                    }
                }
            }
        }

        return constructorDocJson;
    }
}
