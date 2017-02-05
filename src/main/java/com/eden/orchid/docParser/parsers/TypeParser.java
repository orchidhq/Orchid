package com.eden.orchid.docParser.parsers;

import com.sun.javadoc.Type;
import org.json.JSONArray;
import org.json.JSONObject;

public class TypeParser {

    public static JSONObject getTypeObject(Type type) {
        if (type == null) {
            return null;
        }

        JSONObject typeObject = new JSONObject();

        typeObject.put("name", type.simpleTypeName());
        typeObject.put("qualifiedName", type.qualifiedTypeName());
        typeObject.put("dimension", type.dimension());

        if (type.asParameterizedType() != null) {
            typeObject.put("typeParameters", new JSONArray());

            for (Type typeParameter : type.asParameterizedType().typeArguments()) {
                JSONObject nestedType = getTypeObject(typeParameter);
                typeObject.getJSONArray("typeParameters").put(nestedType);
            }
        }

        if (type.asWildcardType() != null) {
            typeObject.put("wildcardType", new JSONObject());

            if (type.asWildcardType().extendsBounds().length > 0) {
                typeObject.getJSONObject("wildcardType").put("extends", new JSONArray());
                for (Type typeParameter : type.asWildcardType().extendsBounds()) {
                    JSONObject nestedType = getTypeObject(typeParameter);
                    typeObject.getJSONObject("wildcardType").getJSONArray("extends").put(nestedType);
                }
            }
            if (type.asWildcardType().superBounds().length > 0) {
                typeObject.getJSONObject("wildcardType").put("super", new JSONArray());
                for (Type typeParameter : type.asWildcardType().superBounds()) {
                    JSONObject nestedType = getTypeObject(typeParameter);
                    typeObject.getJSONObject("wildcardType").getJSONArray("super").put(nestedType);
                }
            }
        }

        return typeObject;
    }
}
