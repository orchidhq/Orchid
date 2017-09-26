package com.eden.orchid.impl.compilers.jtwig;

import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Singleton
public final class LimitFilter implements JtwigFunction {

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

        try {
            if(fnParams.size() == 2) {
                List list = (List) fnParams.get(0);
                int count = Integer.parseInt(fnParams.get(1).toString());

                if(count >= list.size()) {
                    count = list.size();
                }

                return list.subList(0, count);
            }
            else if(fnParams.size() == 3) {
                List list = (List) fnParams.get(0);
                int count = Integer.parseInt(fnParams.get(1).toString());
                int page = Integer.parseInt(fnParams.get(2).toString());

                int offset = (page - 1) * count;
                int offsetEnd = offset + count;

                if(offset >= list.size()) {
                    offset = list.size() - 1;
                    offsetEnd = offset;
                }

                if(offsetEnd >= list.size()) {
                    offsetEnd = list.size();
                }

                if(offset >= 0) {
                    return list.subList(offset, offsetEnd);
                }
                else {
                    return list.subList(0, offsetEnd);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
