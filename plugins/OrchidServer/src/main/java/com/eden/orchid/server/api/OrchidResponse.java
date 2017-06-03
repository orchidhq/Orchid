package com.eden.orchid.server.api;

import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;

public class OrchidResponse {

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
        this.response = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, content, mimeType);
    }

    public OrchidResponse(NanoHTTPD.Response response) {
        this.response = response;
    }

    public NanoHTTPD.Response getResponse() {
        return response;
    }
}
