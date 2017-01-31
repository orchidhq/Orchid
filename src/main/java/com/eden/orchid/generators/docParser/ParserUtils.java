package com.eden.orchid.generators.docParser;

import com.eden.orchid.Orchid;
import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;
import org.json.JSONArray;
import org.json.JSONObject;

public class ParserUtils {
    public static JSONObject getCommentDescription(Doc doc) {
        JSONObject comment = new JSONObject();

        Tag[] firstSentenceTags = doc.firstSentenceTags();

        String firstSentence = "";

        for (Tag tag : firstSentenceTags) {
            firstSentence += tag.text();
        }

        comment.put("shortDescription", firstSentence);
        comment.put("description", doc.commentText());

        return comment;
    }

    public static JSONObject getTypeObject(Type type) {
        if (type == null) {
            return null;
        }

        JSONObject typeObject = new JSONObject();

        typeObject.put("name", type.qualifiedTypeName());
        typeObject.put("simpleName", type.simpleTypeName());
        typeObject.put("dimension", type.dimension());

        try {
            if(Orchid.query("index.classes") != null) {
                JSONArray classIndex = (JSONArray) Orchid.query("index.classes.internal").getElement();

                for (int i = 0; i < classIndex.length(); i++) {
                    if (classIndex.getJSONObject(i).getString("name").equals(type.qualifiedTypeName())) {
                        typeObject.put("url", classIndex.getJSONObject(i).getString("url"));
                        typeObject.put("scope", "internal");
                        break;
                    }
                }

                classIndex = (JSONArray) Orchid.query("index.classes.external").getElement();

                for (int i = 0; i < classIndex.length(); i++) {
                    if (classIndex.getJSONObject(i).getString("name").equals(type.qualifiedTypeName())) {
                        typeObject.put("url", classIndex.getJSONObject(i).getString("url"));
                        typeObject.put("scope", "external");
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

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
