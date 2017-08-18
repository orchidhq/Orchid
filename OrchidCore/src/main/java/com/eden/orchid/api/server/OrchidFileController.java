package com.eden.orchid.api.server;

import fi.iki.elonen.NanoHTTPD;

public interface OrchidFileController  {

    NanoHTTPD.Response findFile(String targetPath);

}
