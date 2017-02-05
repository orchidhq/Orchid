package com.eden.orchid.impl.compilers.jtwig;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.Orchid;
import com.eden.orchid.utilities.AutoRegister;
import org.json.JSONObject;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@AutoRegister
public class TwigLinkFilter implements JtwigFunction {

    @Override
    public String name() {
        return "link";
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

    public Object apply(Object value) {

        String s = value.toString();

        if(Orchid.query("index.internalClasses") != null) {

            JSONObject internalClasses = (JSONObject) Orchid.query("index.internalClasses").getElement();

            List urls = TwigWalkMapFilter.walkObject(internalClasses, "url");

            for(Object object : urls) {
                if (object instanceof Map) {
                    Map map = (Map) object;

                    if(map.containsKey("name") && map.containsKey("url") && map.get("name").toString().equals(value.toString())) {
                        return Clog.format("<a class=\"internal\" href=\"#{$1}\">#{$2}</a>", map.get("url"), map.get("name"));
                    }
                }
            }
        }

        if(Orchid.query("index.internalClasses") != null) {

            JSONObject externalClasses = (JSONObject) Orchid.query("index.externalClasses").getElement();

            List urls = TwigWalkMapFilter.walkObject(externalClasses, "url");

            for(Object object : urls) {
                if (object instanceof Map) {
                    Map map = (Map) object;

                    if(map.containsKey("name") && map.containsKey("url") && map.get("name").toString().equals(value.toString())) {
                        return Clog.format("<a class=\"internal\" href=\"#{$1}\">#{$2}</a>", map.get("url"), map.get("name"));
                    }
                }
            }
        }

        return s;
    }
}