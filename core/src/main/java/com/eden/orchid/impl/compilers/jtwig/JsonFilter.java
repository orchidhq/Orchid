package com.eden.orchid.impl.compilers.jtwig;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Singleton
public class JsonFilter implements JtwigFunction {

    @Override
    public String name() {
        return "json";
    }

    @Override
    public Collection<String> aliases() {
        return Collections.emptyList();
    }

    @Override
    public Object execute(FunctionRequest request) {

        List<Object> fnParams = request.maximumNumberOfArguments(1)
                                       .minimumNumberOfArguments(1)
                                       .getArguments();

        Object value = fnParams.get(0);

        return apply(value);
    }

    public Object apply(Object value, Object... params) {

        if(value instanceof JSONObject) {
            return ((JSONObject) value).toString(2);
        }
        else if(value instanceof JSONArray) {
            return ((JSONArray) value).toString(2);
        }
        else if(value instanceof Map) {
            return new JSONObject((Map) value).toString(2);
        }
        else if(value instanceof Collection) {
            return new JSONArray((Collection) value).toString(2);
        }

        return value;
    }
}