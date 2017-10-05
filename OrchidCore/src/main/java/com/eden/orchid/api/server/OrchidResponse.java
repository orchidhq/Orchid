package com.eden.orchid.api.server;

import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;

public final class OrchidResponse {

    private NanoHTTPD.Response response;

    public OrchidResponse(String content) {
        this.response = NanoHTTPD.newFixedLengthResponse(content);
    }

    public OrchidResponse(JSONObject data) {
        this.response = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, data.toString(), "application/json");
    }

    public OrchidResponse(JSONArray data) {
        this.response = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, data.toString(), "application/json");
    }

    public OrchidResponse(String content, String mimeType) {
        this.response = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mimeType, content);
    }

    public OrchidResponse(NanoHTTPD.Response response) {
        this.response = response;
    }

    public NanoHTTPD.Response getResponse() {
        response.addHeader("Access-Control-Allow-Origin", "*");
        return response;
    }
}
