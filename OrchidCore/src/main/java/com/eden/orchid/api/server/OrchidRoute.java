package com.eden.orchid.api.server;

import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.utilities.OrchidUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class OrchidRoute {
    private final OrchidController controller;
    private final Method method;
    private final String namespace;
    private final String path;
    private Class<? extends OptionsHolder> paramsClass;

    public OrchidRoute(OrchidController controller, Method method, String path, Class<? extends OptionsHolder> paramsClass) {
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
            if (hasParamsClass()) {
                OptionsHolder params = request.getContext().resolve(this.paramsClass);
                params.extractOptions(request.getContext(), request.all());
                methodParameters.add(params);
            }
            for (String pathKey : ServerUtils.getPathParams(path)) {
                methodParameters.add(request.path(pathKey));
            }
            Object[] methodParametersArray = new Object[methodParameters.size()];
            methodParameters.toArray(methodParametersArray);
            return (OrchidResponse) method.invoke(controller, methodParametersArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public OrchidController getController() {
        return this.controller;
    }

    public Method getMethod() {
        return this.method;
    }

    public String getNamespace() {
        return this.namespace;
    }
}
