package com.eden.orchid.api.server;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import fi.iki.elonen.NanoHTTPD;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public final class OrchidResponse {

    private final OrchidContext context;

    @Getter private InputStream contentStream;
    @Getter private long contentStreamLength;
    @Getter @Setter private String content;
    @Getter private String json;

    @Getter @Setter private OrchidView view;

    @Getter private NanoHTTPD.Response.Status status;
    @Getter @Setter private String mimeType;
    @Getter @Setter private Map<String, String> headers;

    private NanoHTTPD.Response response;

    public OrchidResponse(OrchidContext context) {
        this.context = context;
    }

// Builder Helpers
//----------------------------------------------------------------------------------------------------------------------

    public OrchidResponse json(JSONObject json) {
        this.json = json.toString();

        return this;
    }

    public OrchidResponse json(Map<String, ?> json) {
        this.json = new JSONObject(json).toString();

        return this;
    }

    public OrchidResponse json(JSONArray json) {
        this.json = json.toString();

        return this;
    }

    public OrchidResponse json(Collection<?> json) {
        this.json = new JSONArray(json).toString();

        return this;
    }

    public OrchidResponse contentStream(InputStream contentStream) {
        try {
            this.contentStreamLength = contentStream.available();
            this.contentStream = contentStream;
        }
        catch (IOException e) {
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

// Builder get result
//----------------------------------------------------------------------------------------------------------------------

    public NanoHTTPD.Response getResponse() {
        NanoHTTPD.Response response = null;

        // create response
        try {
            if(this.response != null) {
                response = this.response;
            }
            else if (view != null) {
                response = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", view.renderView());
            }
            else if (!EdenUtils.isEmpty(content)) {
                response = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", content);
            }
            else if (!EdenUtils.isEmpty(json)) {
                response = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", json);
            }
            else if (contentStream != null) {
                response = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.OK, mimeType, contentStream, contentStream.available());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(response == null) {
            response = NanoHTTPD.newFixedLengthResponse("No Data");
        }

        // set headers
        response.addHeader("Access-Control-Allow-Origin", "*");
        if(headers != null) {
            for(String header : headers.keySet()) {
                response.addHeader(header, headers.get(header));
            }
        }

        // set status
        if(status != null) {
            response.setStatus(status);
        }

        // set mime type
        if(mimeType != null) {
            response.setMimeType(mimeType);
        }

        return response;
    }

}
