package com.eden.orchid.impl.docParser.docs;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ProgramElementDoc;
import org.json.JSONArray;
import org.json.JSONObject;

public class AnnotationParser {

    public JSONArray getAnnotations(ProgramElementDoc doc) {
        JSONArray annotations = new JSONArray();

        for(AnnotationDesc a : doc.annotations()) {
            JSONObject object = new JSONObject();

            if(object.length() > 0) {
                annotations.put(object);
            }
        }

        return (annotations.length() > 0) ? annotations : null;
    }
}
