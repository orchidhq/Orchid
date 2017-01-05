package com.eden.orchid.generators.docParser;

import com.sun.javadoc.FieldDoc;
import org.json.JSONObject;

public class FieldDocParser {
    public static JSONObject getFieldObject(FieldDoc fieldDoc) {
        JSONObject fieldDocJson = ParserUtils.getTypeObject(fieldDoc.type());
        fieldDocJson.put("modifiers", fieldDoc.modifiers());

        fieldDocJson.put("variableName", fieldDoc.name());

        if(fieldDoc.commentText().length() > 0) {
            fieldDocJson.put("comment", ParserUtils.getCommentDescription(fieldDoc));
        }

        return fieldDocJson;
    }
}
