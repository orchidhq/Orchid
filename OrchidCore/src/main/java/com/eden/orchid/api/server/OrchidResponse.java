package com.eden.orchid.api.server;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class OrchidResponse {
    private final OrchidContext context;
    private InputStream contentStream;
    private long contentStreamLength;
    private String content;
    private String json;
    private OrchidView view;
    private NanoHTTPD.Response.Status status;
    private String mimeType;
    private Map<String, String> headers;
    private NanoHTTPD.Response response;

    public OrchidResponse(OrchidContext context) {
        this.context = context;
    }

// Builder Helpers
//----------------------------------------------------------------------------------------------------------------------
    public OrchidResponse json(JSONObject json) {
        this.json = json.toString(2);
        return this;
    }

    public OrchidResponse json(Map<String, ?> json) {
        this.json = new JSONObject(json).toString(2);
        return this;
    }

    public OrchidResponse json(JSONArray json) {
        this.json = json.toString(2);
        return this;
    }

    public OrchidResponse json(Collection<?> json) {
        this.json = new JSONArray(json).toString(2);
        return this;
    }

    public OrchidResponse contentStream(InputStream contentStream) {
        try {
            this.contentStreamLength = contentStream.available();
            this.contentStream = contentStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public OrchidResponse contentStream(InputStream contentStream, long contentStreamLength) {
        this.contentStream = contentStream;
        this.contentStreamLength = contentStreamLength;
        return this;
    }

    public OrchidResponse status(int status) {
        this.status = NanoHTTPD.Response.Status.lookup(status);
        return this;
    }

    public OrchidResponse header(String header, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(header, value);
        return this;
    }

// Builder get result
//----------------------------------------------------------------------------------------------------------------------
    public NanoHTTPD.Response getResponse() {
        NanoHTTPD.Response response = null;
        // create response
        try {
            if (this.response != null) {
                response = this.response;
            } else if (view != null) {
                response = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", view.renderInLayout());
            } else if (!EdenUtils.isEmpty(content)) {
                response = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", content);
            } else if (!EdenUtils.isEmpty(json)) {
                response = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", json);
            } else if (contentStream != null) {
                response = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mimeType, contentStream, contentStream.available());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response == null) {
            response = NanoHTTPD.newFixedLengthResponse("No Data");
        }
        // set headers
        response.addHeader("Access-Control-Allow-Origin", "*");
        if (headers != null) {
            for (String header : headers.keySet()) {
                response.addHeader(header, headers.get(header));
            }
        }
        // set status
        if (status != null) {
            response.setStatus(status);
        }
        // set mime type
        if (mimeType != null) {
            response.setMimeType(mimeType);
        }
        return response;
    }

    public InputStream contentStream() {
        return this.contentStream;
    }

    public long contentStreamLength() {
        return this.contentStreamLength;
    }

    public String content() {
        return this.content;
    }

    public OrchidResponse content(final String content) {
        this.content = content;
        return this;
    }

    public String json() {
        return this.json;
    }

    public OrchidView view() {
        return this.view;
    }

    public OrchidResponse view(final OrchidView view) {
        this.view = view;
        return this;
    }

    public NanoHTTPD.Response.Status status() {
        return this.status;
    }

    public String mimeType() {
        return this.mimeType;
    }

    public OrchidResponse mimeType(final String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public Map<String, String> headers() {
        return this.headers;
    }

    public OrchidResponse headers(final Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public OrchidResponse response(final NanoHTTPD.Response response) {
        this.response = response;
        return this;
    }
}
