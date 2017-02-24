package com.eden.orchid.impl.compilers.jtwig;

import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Singleton
public class LimitFilter implements JtwigFunction {

    @Override
    public String name() {
        return "limit";
    }

    @Override
    public Collection<String> aliases() {
        return Collections.emptyList();
    }

    @Override
    public Object execute(FunctionRequest request) {

        List<Object> fnParams = request.maximumNumberOfArguments(3)
                                     .minimumNumberOfArguments(2)
                                     .getArguments();

        if(fnParams.size() == 2) {
            List list = (List) fnParams.get(0);
            int count = Integer.parseInt(fnParams.get(1).toString());

            return list.subList(0, count);
        }
        else if(fnParams.size() == 3) {
            List list = (List) fnParams.get(0);
            int count = Integer.parseInt(fnParams.get(1).toString());
            int page = Integer.parseInt(fnParams.get(2).toString());
            int offset = (page - 1) * count;
            
            return list.subList(offset, offset + count);
        }

        return null;
    }
}
