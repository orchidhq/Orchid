package com.eden.orchid.posts;

import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SortPostsFilter implements JtwigFunction {

    @Override
    public String name() {
        return "sortPosts";
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

        List<Map<String, ?>> posts = (List<Map<String, ?>>) fnParams.get(0);

        posts.sort((o1, o2) -> {
            String[] criteria = new String[]{"year", "month", "day", "hour", "minute", "second"};

            if (o1.containsKey("data") && o2.containsKey("data")
                    && (o1.get("data") != null) && (o2.get("data") != null)
                    && (o1.get("data") instanceof Map) && (o2.get("data") instanceof Map)) {

                o1 = (Map<String, ?>) o1.get("data");
                o2 = (Map<String, ?>) o2.get("data");
            }

            for (String item : criteria) {
                if (o1.containsKey(item) && o2.containsKey(item) && (o1.get(item) != null) && (o2.get(item) != null)) {
                    int result = Integer.parseInt(o2.get(item).toString()) - Integer.parseInt(o1.get(item).toString());

                    if (result != 0) {
                        return result;
                    }
                }
            }

            return 0;

        });

        return posts;
    }
}
