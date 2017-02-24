package com.eden.orchid.impl.compilers.jtwig;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.utilities.OrchidUtils;
import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.JtwigFunction;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Singleton
public class LinkInFilter implements JtwigFunction {

    private OrchidContext context;

    @Inject
    public LinkInFilter(OrchidContext context) {
        this.context = context;
    }

    @Override
    public String name() {
        return "linkIn";
    }

    @Override
    public Collection<String> aliases() {
        return Collections.emptyList();
    }

    @Override
    public Object execute(FunctionRequest request) {
        List<Object> fnParams = request.minimumNumberOfArguments(2)
                                       .maximumNumberOfArguments(3)
                                       .getArguments();

        if(fnParams.size() == 2) {
            String linkName = fnParams.get(0).toString();
            String indexKey = fnParams.get(1).toString();

            return OrchidUtils.linkToIndex(context, linkName, indexKey);
        }
        else if(fnParams.size() == 3) {
            String linkName    = fnParams.get(0).toString();
            String indexKey    = fnParams.get(1).toString();
            String displayName = fnParams.get(2).toString();

            return OrchidUtils.linkToIndex(context, linkName, indexKey, displayName);
        }

        return "";
    }
}