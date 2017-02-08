package com.eden.orchid.impl.compilers.jtwig;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.registration.AutoRegister;
import org.json.JSONObject;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@AutoRegister
public class TwigWalkMapFilter implements JtwigFunction {

    @Override
    public String name() {
        return "walk";
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
        String stop = null;
        if(params != null && params[0] != null) {
            stop = params[0].toString();
        }

        return walk(value, stop);
    }

    private static List walk(Object value, String stop) {
        List foundElements = new ArrayList<>();

        if(value instanceof Map) {
            Map<String, ?> map = (Map<String, ?>) value;

            if(!EdenUtils.isEmpty(stop) && map.containsKey(stop)) {
                foundElements.add(map);
            }
            else {
                for(String key : map.keySet()) {
                    List keysElements = walk(map.get(key), stop);
                    foundElements.addAll(keysElements);
                }
            }
        }
        else if(value instanceof Collection) {
            Collection collection = (Collection) value;

            for(Object object : collection) {
                List collectionElements = walk(object, stop);
                foundElements.addAll(collectionElements);
            }
        }
        else {
            foundElements.add(value);
        }

        return foundElements;
    }

    public static List walkObject(JSONObject object, String stop) {
        return walk(object.toMap(), stop);
    }
}