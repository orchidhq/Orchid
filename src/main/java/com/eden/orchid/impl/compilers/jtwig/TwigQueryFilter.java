package com.eden.orchid.impl.compilers.jtwig;

import com.eden.common.json.JSONElement;
import com.eden.orchid.utilities.AutoRegister;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@AutoRegister
public class TwigQueryFilter implements JtwigFunction {

    @Override
    public String name() {
        return "query";
    }

    @Override
    public Collection<String> aliases() {
        return Collections.emptyList();
    }

    @Override
    public Object execute(FunctionRequest request) {

        List<Object> fnParams = request.getArguments();

        Object value = fnParams.get(0);
        Object[] params = new Object[]{fnParams.get(1)};

        return apply(value, params);
    }


    public Object apply(Object value, Object... params) {
        if(params != null && params[0] != null) {
            String pointer = params[0].toString();

            if(value instanceof Map) {
                JSONObject mapObject = new JSONObject((Map<String, ?>) value);

                JSONElement el = new JSONElement(mapObject);

                el = el.query(pointer);

                if(el != null) {

                    if(el.getElement() instanceof JSONObject) {
                        return ((JSONObject) el.getElement()).toMap();
                    }
                    else if(el.getElement() instanceof JSONArray) {
                        return ((JSONArray) el.getElement()).toList();
                    }
                }
            }
            else if(value instanceof Collection) {
                JSONArray collectionObject = new JSONArray(value);

                JSONElement el = new JSONElement(collectionObject).query(pointer);

                if(el != null) {
                    if(el.getElement() instanceof JSONObject) {
                        return ((JSONObject) el.getElement()).toMap();
                    }
                    else if(el.getElement() instanceof JSONArray) {
                        return ((JSONArray) el.getElement()).toList();
                    }

                }
            }
        }

        return null;
    }
}
