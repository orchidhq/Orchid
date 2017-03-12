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
public class LinkFilter implements JtwigFunction {

    private OrchidContext context;

    @Inject
    public LinkFilter(OrchidContext context) {
        this.context = context;
    }

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
        List<Object> fnParams = request.minimumNumberOfArguments(1)
                                       .maximumNumberOfArguments(2)
                                       .getArguments();

        if(fnParams.size() == 1 && fnParams.get(0) != null) {
            String linkName = fnParams.get(0).toString();

            return OrchidUtils.linkTo(context, linkName);
        }
        else if(fnParams.size() == 2 && fnParams.get(0) != null && fnParams.get(1) != null) {
            String linkName = fnParams.get(0).toString();
            String displayName = fnParams.get(1).toString();

            return OrchidUtils.linkTo(context, linkName, displayName);
        }

        return "";
    }
}