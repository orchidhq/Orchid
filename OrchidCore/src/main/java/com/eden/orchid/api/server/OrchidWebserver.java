package com.eden.orchid.api.server;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.OrchidFlags;
import com.eden.orchid.api.server.annotations.AdminPage;
import com.eden.orchid.api.server.annotations.Delete;
import com.eden.orchid.api.server.annotations.Get;
import com.eden.orchid.api.server.annotations.Post;
import com.eden.orchid.api.server.annotations.Put;
import com.eden.orchid.utilities.OrchidUtils;
import fi.iki.elonen.NanoHTTPD;
import kotlin.Pair;
import kotlin.collections.MapsKt;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class OrchidWebserver extends NanoHTTPD {
    private static Class<? extends Annotation>[] annotationClasses = new Class[]{Get.class, Post.class, Put.class, Delete.class};
    private final OrchidContext context;
    private final Set<OrchidController> controllers;
    private final OrchidFileController fileController;
    private final List<OrchidRoute> getRoutes;
    private final List<OrchidRoute> postRoutes;
    private final List<OrchidRoute> putRoutes;
    private final List<OrchidRoute> deleteRoutes;
    private final List<AdminPageRoute> adminRoutes;

    // taken from https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Complete_list_of_MIME_types
    public static Map<String, String> mimeTypes = MapsKt.mapOf(
            new Pair<>("aac", "audio/aac"),
            new Pair<>("bmp", "image/bmp"),
            new Pair<>("css", "text/css"),
            new Pair<>("csv", "text/csv"),
            new Pair<>("epub", "application/epub+zip"),
            new Pair<>("gz", "application/gzip"),
            new Pair<>("gif", "image/gif"),
            new Pair<>("htm", "text/html"),
            new Pair<>("html", "text/html"),
            new Pair<>("ico", "image/vnd.microsoft.icon"),
            new Pair<>("jpeg", "image/jpeg"),
            new Pair<>("jpg", "image/jpeg"),
            new Pair<>("js", "text/javascript"),
            new Pair<>("json", "application/json"),
            new Pair<>("mp3", "audio/mpeg"),
            new Pair<>("mpeg", "video/mpeg"),
            new Pair<>("oga", "audio/ogg"),
            new Pair<>("ogv", "video/ogg"),
            new Pair<>("ogx", "application/ogg"),
            new Pair<>("opus", "audio/opus"),
            new Pair<>("otf", "font/otf"),
            new Pair<>("png", "image/png"),
            new Pair<>("pdf", "application/pdf"),
            new Pair<>("svg", "image/svg+xml"),
            new Pair<>("tif", "image/tiff"),
            new Pair<>("tiff", "image/tiff"),
            new Pair<>("txt", "text/plain"),
            new Pair<>("weba", "audio/webm"),
            new Pair<>("webm", "video/webm"),
            new Pair<>("webp", "image/webp"),
            new Pair<>("woff", "font/woff"),
            new Pair<>("woff2", "font/woff2"),
            new Pair<>("xml", "application/xml"),
            new Pair<>("zip", "application/zip")
    );

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
        adminRoutes = new ArrayList<>();
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

    private OrchidRoute createRoute(OrchidController controller, java.lang.reflect.Method method, String path, Class<? extends OptionsHolder> paramsClass) {
        OrchidRoute route = new OrchidRoute(controller, method, path, paramsClass);
        validateControllerMethod(method, route);
        return route;
    }

    private void register(OrchidController controller, Get methodAnnotation, java.lang.reflect.Method method) {
        OrchidRoute route = createRoute(
                controller,
                method,
                methodAnnotation.path(),
                methodAnnotation.params()
        );
        if (method.isAnnotationPresent(AdminPage.class)) {
            AdminPage adminPage = method.getAnnotation(AdminPage.class);
            adminRoutes.add(new AdminPageRoute(
                    route,
                    adminPage.value(),
                    adminPage.icon()
            ));
        }
        getRoutes.add(route);
    }

    private void register(OrchidController controller, Post methodAnnotation, java.lang.reflect.Method method) {
        postRoutes.add(
                createRoute(
                        controller,
                        method,
                        methodAnnotation.path(),
                        methodAnnotation.params()
                )
        );
    }

    private void register(OrchidController controller, Put methodAnnotation, java.lang.reflect.Method method) {
        putRoutes.add(
                createRoute(
                        controller,
                        method,
                        methodAnnotation.path(),
                        methodAnnotation.params()
                )
        );
    }

    private void register(OrchidController controller, Delete methodAnnotation, java.lang.reflect.Method method) {
        deleteRoutes.add(
                createRoute(
                        controller,
                        method,
                        methodAnnotation.path(),
                        methodAnnotation.params()
                )
        );
    }

    private void validateControllerMethod(java.lang.reflect.Method method, OrchidRoute route) {
        String path = route.getPath();
        Class<? extends OptionsHolder> paramsClass = route.getParamsClass();
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
                Boolean legacyFileServer = OrchidFlags.getInstance().getFlagValue("legacyFileServer");
                if (legacyFileServer != null && legacyFileServer) {
                    response = fileController.findFile(context, OrchidUtils.normalizePath(route));
                } else {
                    response = fileController.findPage(context, OrchidUtils.normalizePath(route));
                }
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

    public List<AdminPageRoute> getAdminRoutes() {
        return adminRoutes;
    }
}
