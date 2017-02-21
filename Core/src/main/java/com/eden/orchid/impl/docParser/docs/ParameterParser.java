package com.eden.orchid.impl.docParser.docs;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

public class ParameterParser {

    private TypeParser typeParser;

    @Inject
    public ParameterParser(TypeParser typeParser) {
        this.typeParser = typeParser;
    }

    public JSONArray getParameters(ExecutableMemberDoc doc) {
        JSONArray array = new JSONArray();

        for(Parameter parameter : doc.parameters()) {
            JSONObject object = new JSONObject();

            object.put("type", typeParser.getTypeObject(parameter.type()));
            object.put("name", parameter.name());

            for (ParamTag paramTag : doc.paramTags()) {
                if (parameter.name().equals(paramTag.parameterName())) {
                    object.put("description", paramTag.parameterComment());
                }
            }

            array.put(object);
        }

        return (array.length() > 0) ? array : null;
    }
}
