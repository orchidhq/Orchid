package com.eden.orchid.server.api;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.utilities.OrchidUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ServerUtils {

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
}
