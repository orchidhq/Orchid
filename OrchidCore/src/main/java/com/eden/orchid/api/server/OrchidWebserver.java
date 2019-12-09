package com.eden.orchid.api.server;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.server.annotations.Delete;
import com.eden.orchid.api.server.annotations.Get;
import com.eden.orchid.api.server.annotations.Post;
import com.eden.orchid.api.server.annotations.Put;
import com.eden.orchid.utilities.OrchidUtils;
import fi.iki.elonen.NanoHTTPD;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class OrchidWebserver extends NanoHTTPD {
    private static Class<? extends Annotation>[] annotationClasses = new Class[] {Get.class, Post.class, Put.class, Delete.class};
    private final OrchidContext context;
    private final Set<OrchidController> controllers;
    private final OrchidFileController fileController;
    private final List<OrchidRoute> getRoutes;
    private final List<OrchidRoute> postRoutes;
    private final List<OrchidRoute> putRoutes;
    private final List<OrchidRoute> deleteRoutes;

    public OrchidWebserver(OrchidContext context, Set<OrchidController> controllers, OrchidFileController fileController, int port) throws IOException {
        super(ServerUtils.getNearestFreePort(port));
        this.context = context;
        this.controllers = controllers;
        this.fileController = fileController;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        context.setBaseUrl(Clog.format("http://localhost:{}/", getListeningPort()));
        Clog.i(getServerRunningMessage());
        getRoutes = new ArrayList<>();
        postRoutes = new ArrayList<>();
        putRoutes = new ArrayList<>();
        deleteRoutes = new ArrayList<>();
        for (OrchidController listener : controllers) {
            findRoutes(listener);
        }
    }

    public String getServerRunningMessage() {
        return Clog.format("Webserver Running at {}", context.getBaseUrl());
    }

    private void findRoutes(OrchidController controller) {
        Arrays.stream(controller.getClass().getDeclaredMethods()).filter(method -> {
            for (Class<? extends Annotation> annotationClass : annotationClasses) {
                if (method.isAnnotationPresent(annotationClass)) {
                    return true;
                }
            }
            return false;
        }).forEach(method -> {
            if (method.isAnnotationPresent(Get.class)) {
                register(controller, method.getAnnotation(Get.class), method);
            } else if (method.isAnnotationPresent(Post.class)) {
                register(controller, method.getAnnotation(Post.class), method);
            } else if (method.isAnnotationPresent(Put.class)) {
                register(controller, method.getAnnotation(Put.class), method);
            } else if (method.isAnnotationPresent(Delete.class)) {
                register(controller, method.getAnnotation(Delete.class), method);
            }
        });
    }

    private void register(OrchidController controller, Get methodAnnotation, java.lang.reflect.Method method) {
        String path = methodAnnotation.path();
        Class<? extends OptionsHolder> paramsClass = methodAnnotation.params();
        validateControllerMethod(method, path, paramsClass);
        getRoutes.add(new OrchidRoute(controller, method, path, paramsClass));
    }

    private void register(OrchidController controller, Post methodAnnotation, java.lang.reflect.Method method) {
        String path = methodAnnotation.path();
        Class<? extends OptionsHolder> paramsClass = methodAnnotation.params();
        validateControllerMethod(method, path, paramsClass);
        postRoutes.add(new OrchidRoute(controller, method, path, paramsClass));
    }

    private void register(OrchidController controller, Put methodAnnotation, java.lang.reflect.Method method) {
        String path = methodAnnotation.path();
        Class<? extends OptionsHolder> paramsClass = methodAnnotation.params();
        validateControllerMethod(method, path, paramsClass);
        putRoutes.add(new OrchidRoute(controller, method, path, paramsClass));
    }

    private void register(OrchidController controller, Delete methodAnnotation, java.lang.reflect.Method method) {
        String path = methodAnnotation.path();
        Class<? extends OptionsHolder> paramsClass = methodAnnotation.params();
        validateControllerMethod(method, path, paramsClass);
        deleteRoutes.add(new OrchidRoute(controller, method, path, paramsClass));
    }

    private void validateControllerMethod(java.lang.reflect.Method method, String path, Class<? extends OptionsHolder> paramsClass) {
        if (EdenUtils.isEmpty(path) || !path.startsWith("/")) {
            throw new IllegalArgumentException("OrchidController\'s path must not be empty and must start with a slash");
        }
        path = "/" + OrchidUtils.normalizePath(path);
        if (!method.getReturnType().equals(OrchidResponse.class)) {
            throw new IllegalArgumentException("OrchidController\'s return value must be of type [OrchidResponse]");
        }
        boolean hasParamsClass = !OptionsHolder.class.equals(paramsClass);
        String controllerClass = method.getDeclaringClass().getName();
        String controllerAction = method.getName();
        List<String> pathParams = ServerUtils.getPathParams(path);
        Class<?>[] methodParameters = method.getParameterTypes();
        if (hasParamsClass) {
            if (methodParameters.length != pathParams.size() + 2) {
                throw new IllegalArgumentException(Clog.format("Controller action [{}@{}] must accept a first parameter of type [OrchidRequest], a second parameter of {}, followed by {} [String] parameters for the path parameters", controllerClass, controllerAction, paramsClass.getName(), pathParams.size()));
            }
        } else {
            if (methodParameters.length != pathParams.size() + 1) {
                throw new IllegalArgumentException(Clog.format("Controller action [{}@{}] must accept a first parameter of type [OrchidRequest], followed by {} [String] parameters for the path parameters", controllerClass, controllerAction, pathParams.size()));
            }
        }
        for (int i = 0; i < methodParameters.length; i++) {
            switch (i) {
            case 0: 
                if (!methodParameters[i].equals(OrchidRequest.class)) {
                    throw new IllegalArgumentException(Clog.format("Controller action [{}@{}]\'s first argument must be of type [OrchidRequest]"));
                }
                break;

            case 1: 
                if (hasParamsClass) {
                    if (!methodParameters[i].equals(paramsClass)) {
                        throw new IllegalArgumentException(Clog.format("Controller action [{}@{}]\'s second argument must be of type [{}]", paramsClass.getName()));
                    }
                    break;
                }

            default: 
                if (!methodParameters[i].equals(String.class)) {
                    throw new IllegalArgumentException("Controller action [{}@{}]\'s path parameters must be accepted as type [String]");
                }
                break;
            }
        }
    }

    private OrchidRoute findRoute(String path, List<OrchidRoute> routes) {
        for (OrchidRoute route : routes) {
            String pathRegex = ServerUtils.getPathRegex(route.getPath());
            if (path.matches(pathRegex)) {
                return route;
            }
        }
        return null;
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            Map<String, String> files = new HashMap<>();
            OrchidRoute matchingRoute = null;
            String route = "/" + OrchidUtils.normalizePath(session.getUri());
            Clog.i("Serving: [{}] {}", session.getMethod(), route);
            switch (session.getMethod()) {
            case GET: 
                matchingRoute = findRoute(route, getRoutes);
                break;

            case POST: 
                session.parseBody(files);
                matchingRoute = findRoute(route, postRoutes);
                break;

            case PUT: 
                session.parseBody(files);
                matchingRoute = findRoute(route, putRoutes);
                break;

            case DELETE: 
                matchingRoute = findRoute(route, deleteRoutes);
                break;
            }
            OrchidResponse response;
            if (matchingRoute != null) {
                response = matchingRoute.call(new OrchidRequest(context, session, matchingRoute, files));
            } else {
                response = fileController.findFile(context, OrchidUtils.normalizePath(route));
            }
            if (response != null) {
                return response.getResponse();
            } else {
                return NanoHTTPD.newFixedLengthResponse(Response.Status.NOT_FOUND, "text/html", "");
            }
        } catch (Exception e) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + e.getMessage());
        }
    }

    public OrchidContext getContext() {
        return this.context;
    }

    public Set<OrchidController> getControllers() {
        return this.controllers;
    }

    public OrchidFileController getFileController() {
        return this.fileController;
    }
}
