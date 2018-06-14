package com.eden.orchid.api.server;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class OrchidRoute {

    private final OrchidContext context;

    @Getter private final OrchidController controller;
    @Getter private final Method method;
    @Getter private final String namespace;

    private final String path;
    private Class<? extends OptionsHolder> paramsClass;

    public OrchidRoute(OrchidContext context, OrchidController controller, Method method, String path, Class<? extends OptionsHolder> paramsClass) {
        this.context = context;
        this.controller = controller;
        this.method = method;
        this.path = "/" + OrchidUtils.normalizePath(path);
        this.paramsClass = paramsClass;
        this.namespace = "/" + OrchidUtils.normalizePath(controller.getPathNamespace());
    }

    public String getPath() {
        return "/" + OrchidUtils.normalizePath(this.namespace + this.path);
    }

    private boolean hasParamsClass() {
        return !OptionsHolder.class.equals(paramsClass);
    }

    public OrchidResponse call(OrchidRequest request) {
        try {
            List<Object> methodParameters = new ArrayList<>();
            methodParameters.add(request);

            if(hasParamsClass()) {
                OptionsHolder params = context.resolve(this.paramsClass);
                params.extractOptions(context, request.all());
                methodParameters.add(params);
            }

            for(String pathKey : ServerUtils.getPathParams(path)) {
                methodParameters.add(request.path(pathKey));
            }

            Object[] methodParametersArray = new Object[methodParameters.size()];
            methodParameters.toArray(methodParametersArray);

            return (OrchidResponse) method.invoke(controller, methodParametersArray);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
