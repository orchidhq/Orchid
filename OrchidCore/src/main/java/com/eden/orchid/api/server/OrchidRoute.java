package com.eden.orchid.api.server;

import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrchidRoute {

    private final OrchidController controller;
    private final Method method;
    private final String namespace;
    private final String path;

    public OrchidRoute(OrchidController controller, Method method, String path) {
        this.controller = controller;
        this.method = method;
        this.path = "/" + OrchidUtils.normalizePath(path);
        this.namespace = "/" + OrchidUtils.normalizePath(controller.getPathNamespace());
    }

    public String getPath() {
        return "/" + OrchidUtils.normalizePath(this.namespace + this.path);
    }

    public OrchidResponse call(OrchidRequest request) {
        try {
            List<Object> methodParameters = new ArrayList<>();
            methodParameters.add(request);

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
