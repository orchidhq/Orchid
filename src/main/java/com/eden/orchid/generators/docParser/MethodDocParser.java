package com.eden.orchid.generators.docParser;

import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.Tag;
import org.json.JSONArray;
import org.json.JSONObject;

public class MethodDocParser {
    public static JSONObject getMethodObject(MethodDoc methodDoc) {
        JSONObject methodDocJson = new JSONObject();
        methodDocJson.put("modifiers", methodDoc.modifiers());

        methodDocJson.put("name", methodDoc.name());
        methodDocJson.put("returns", ParserUtils.getTypeObject(methodDoc.returnType()));

        if(methodDoc.parameters().length > 0) {
            methodDocJson.put("parameters", new JSONArray());

            for(Parameter parameter : methodDoc.parameters()) {
                JSONObject parameterObject = ParserUtils.getTypeObject(parameter.type());
                parameterObject.put("variableName", parameter.name());

                methodDocJson.getJSONArray("parameters").put(parameterObject);
            }

            if(methodDoc.isVarArgs()) {
                methodDocJson.getJSONArray("parameters").getJSONObject(methodDocJson.getJSONArray("parameters").length() - 1).put("dimension", "...");
            }
        }

        if(methodDoc.commentText().length() > 0) {
            methodDocJson.put("comment", ParserUtils.getCommentDescription(methodDoc));

            for(ParamTag paramTag : methodDoc.paramTags()) {

                for(int i = 0; i < methodDocJson.getJSONArray("parameters").length(); i++) {
                    JSONObject parameterObject = methodDocJson.getJSONArray("parameters").getJSONObject(i);

                    if(parameterObject.getString("variableName").equals(paramTag.parameterName())) {
                        parameterObject.put("description", paramTag.parameterComment());
                    }
                }
            }

            String returnComment = "";

            for(Tag tag : methodDoc.tags("return")) {
                returnComment += tag.text();
            }
            methodDocJson.getJSONObject("returns").put("description", returnComment);

        }

        return methodDocJson;
    }
}
