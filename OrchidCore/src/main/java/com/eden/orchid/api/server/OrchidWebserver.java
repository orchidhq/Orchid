package com.eden.orchid.api.server;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.server.annotations.Delete;
import com.eden.orchid.api.server.annotations.Get;
import com.eden.orchid.api.server.annotations.Post;
import com.eden.orchid.api.server.annotations.Put;
import com.eden.orchid.utilities.OrchidUtils;
import fi.iki.elonen.NanoHTTPD;
import lombok.Getter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public final class OrchidWebserver extends NanoHTTPD {

    private static Class<? extends Annotation>[] annotationClasses = new Class[]{Get.class, Post.class, Put.class, Delete.class};

    @Getter private final OrchidContext context;

    @Getter private final Set<OrchidController> controllers;
    @Getter private final OrchidFileController fileController;

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
        Clog.i("Webserver Running! Point your browsers to http://localhost:" + getListeningPort() + "/");
        context.setBaseUrl(Clog.format("http://localhost:{}/", getListeningPort()));

        getRoutes = new ArrayList<>();
        postRoutes = new ArrayList<>();
        putRoutes = new ArrayList<>();
        deleteRoutes = new ArrayList<>();

        for (OrchidController listener : controllers) {
            findRoutes(listener);
        }

        Clog.v("Registered #{$1} #{$2} routes", getRoutes.size(), "GET");
        Clog.v("Registered #{$1} #{$2} routes", postRoutes.size(), "POST");
        Clog.v("Registered #{$1} #{$2} routes", putRoutes.size(), "PUT");
        Clog.v("Registered #{$1} #{$2} routes", deleteRoutes.size(), "DELETE");
    }

    private void findRoutes(OrchidController controller) {
        Arrays.stream(controller.getClass().getDeclaredMethods())
              .filter(method -> {
                          for (Class<? extends Annotation> annotationClass : annotationClasses) {
                              if (method.isAnnotationPresent(annotationClass)) {
                                  return true;
                              }
                          }
                          return false;
                      }
              )
              .forEach(method -> {
                  if (method.isAnnotationPresent(Get.class)) {
                      register(controller, method.getAnnotation(Get.class), method);
                  }
                  else if (method.isAnnotationPresent(Post.class)) {
                      register(controller, method.getAnnotation(Post.class), method);
                  }
                  else if (method.isAnnotationPresent(Put.class)) {
                      register(controller, method.getAnnotation(Put.class), method);
                  }
                  else if (method.isAnnotationPresent(Delete.class)) {
                      register(controller, method.getAnnotation(Delete.class), method);
                  }
              });
    }

    private void register(OrchidController controller, Get methodAnnotation, java.lang.reflect.Method method) {
        String path = methodAnnotation.path();
        validateControllerMethod(method, path);
        getRoutes.add(new OrchidRoute(controller, method, path));
    }

    private void register(OrchidController controller, Post methodAnnotation, java.lang.reflect.Method method) {
        String path = methodAnnotation.path();
        validateControllerMethod(method, path);
        postRoutes.add(new OrchidRoute(controller, method, path));
    }

    private void register(OrchidController controller, Put methodAnnotation, java.lang.reflect.Method method) {
        String path = methodAnnotation.path();
        validateControllerMethod(method, path);
        putRoutes.add(new OrchidRoute(controller, method, path));
    }

    private void register(OrchidController controller, Delete methodAnnotation, java.lang.reflect.Method method) {
        String path = methodAnnotation.path();
        validateControllerMethod(method, path);
        deleteRoutes.add(new OrchidRoute(controller, method, path));
    }

    private void validateControllerMethod(java.lang.reflect.Method method, String path) {
        Clog.d("Registering path: '#{$1}", path);

        if (EdenUtils.isEmpty(path) || !path.startsWith("/")) {
            throw new IllegalArgumentException("OrchidController's path must not be empty and must start with a slash");
        }

        path = "/" + OrchidUtils.normalizePath(path);

        if (!method.getReturnType().equals(OrchidResponse.class)) {
            throw new IllegalArgumentException("OrchidController's return value must be of type [OrchidResponse]");
        }

        List<String> pathParams = ServerUtils.getPathParams(path);
        Class<?>[] methodParameters = method.getParameterTypes();
        if (methodParameters.length != pathParams.size() + 1) {
            throw new IllegalArgumentException("OrchidController's parameters must accept a first parameter of type [OrchidRequest] followed by " + pathParams.size() + "[String] parameters for the path parameters");
        }

        for (int i = 0; i < methodParameters.length; i++) {
            if (i == 0) {
                if (!methodParameters[i].equals(OrchidRequest.class)) {
                    throw new IllegalArgumentException("OrchidController's first argument must be of type [OrchidRequest]");
                }
            }
            else {
                if (!methodParameters[i].equals(String.class)) {
                    throw new IllegalArgumentException("OrchidController's path parameters must be accepted as type [String]");
                }
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
        OrchidRoute matchingRoute = null;

        String route = "/" + OrchidUtils.normalizePath(session.getUri());

        switch (session.getMethod()) {
            case GET:
                matchingRoute = findRoute(route, getRoutes);
                break;
            case POST:
                matchingRoute = findRoute(route, postRoutes);
                break;
            case PUT:
                matchingRoute = findRoute(route, putRoutes);
                break;
            case DELETE:
                matchingRoute = findRoute(route, deleteRoutes);
                break;
        }

        if (matchingRoute != null) {
            return matchingRoute.call(new OrchidRequest(session, matchingRoute)).getResponse();
        }
        else {
            return fileController.findFile(OrchidUtils.normalizePath(route));
        }
    }

}
