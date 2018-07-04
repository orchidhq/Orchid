package com.eden.orchid.impl.compilers.clog;

import com.caseyjbrooks.clog.parseltongue.Incantation;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.google.inject.Provider;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public final class ClogIncantationWrapper implements Incantation {

    private final Provider<OrchidContext> contextProvider;
    private final String name;
    private final List<String> params;
    private final Class<? extends TemplateFunction> functionClass;

    public ClogIncantationWrapper(Provider<OrchidContext> contextProvider, String name, List<String> params, Class<? extends TemplateFunction> functionClass) {
        this.contextProvider = contextProvider;
        this.name = name;
        if(params.size() > 0) {
            this.params = params.subList(1, params.size());
        }
        else {
            this.params = params;
        }
        this.functionClass = functionClass;
    }

    @Override
    public Object call(Object input, Object... reagents) {
        TemplateFunction freshFunction = contextProvider.get().getInjector().getInstance(functionClass);

        int length = (!EdenUtils.isEmpty(reagents) && !EdenUtils.isEmpty(params))
                ? Math.min(reagents.length, params.size())
                : 0;

        Map<String, Object> object = new HashMap<>();
        for (int i = 0; i < length; i++) {
            object.put(params.get(i), reagents[i]);
        }

        freshFunction.extractOptions(contextProvider.get(), object);

        return freshFunction.apply(input);
    }
}
