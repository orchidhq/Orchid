package com.eden.orchid.api.server;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.utilities.OrchidUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ServerUtils {

    public static int getNearestFreePort(int rangeMin) {
        int rangeMax = Integer.min(rangeMin + 1000, 65535);
        ServerSocket socket = null;
        for (int i = rangeMin; i <= rangeMax; i++) {
            try {
                socket = new ServerSocket(i);
                socket.close();
                break;
            }
            catch (IOException e) {
            }
        }

        if (socket != null) {
            return socket.getLocalPort();
        }
        else {
            throw new IllegalStateException(Clog.format("Could not find a free port to allocate within range #{$1}-#{$2}.", rangeMin, rangeMax));
        }
    }

    public static List<String> getPathParams(String path) {
        List<String> pathParams = new ArrayList<>();
        for(String pathPiece : path.split("/")) {
            if(pathPiece.startsWith(":")) {
                pathParams.add(pathPiece.substring(1));
            }
            else if((pathPiece.startsWith("{") && pathPiece.endsWith("}"))) {
                pathParams.add(pathPiece.substring(1, pathPiece.length() - 2));
            }
        }

        return pathParams;
    }

    public static String getPathRegex(String path) {
        String regex = "";
        for(String pathPiece : OrchidUtils.normalizePath(path).split("/")) {
            if(pathPiece.startsWith(":")) {
                regex += "/\\w+";
            }
            else if((pathPiece.startsWith("{") && pathPiece.endsWith("}"))) {
                regex += "/\\w+";
            }
            else {
                regex += "/" + pathPiece;
            }
        }

        return regex ;
    }

    public static Map<String, String> parsePathParams(String routePath, String calledPath) {
        String[] routePathPieces = routePath.split("/");
        String[] calledPathPieces = calledPath.split("/");
        if(routePathPieces.length != calledPathPieces.length) {
            throw new IllegalArgumentException("Route path and called path do not match");
        }

        Map<String, String> pathParams = new HashMap<>();

        for (int i = 0; i < routePathPieces.length; i++) {
            if(routePathPieces[i].startsWith(":")) {
                String key = routePathPieces[i].substring(1);
                String value = calledPathPieces[i];
                pathParams.put(key, value);
            }
            else if((routePathPieces[i].startsWith("{") && routePathPieces[i].endsWith("}"))) {
                String key = routePathPieces[i].substring(1, routePathPieces[i].length() - 2);
                String value = calledPathPieces[i];
                pathParams.put(key, value);
            }
        }

        return pathParams;
    }
}
