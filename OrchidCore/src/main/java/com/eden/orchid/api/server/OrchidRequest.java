package com.eden.orchid.api.server;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.utilities.OrchidUtils;
import fi.iki.elonen.NanoHTTPD;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public final class OrchidRequest {

    private OrchidContext context;
    private OrchidRoute route;
    private NanoHTTPD.IHTTPSession session;

    private Map<String, String> pathParams;
    private Map<String, String> files;
    private Map<String, Object> body;

    public OrchidRequest(OrchidContext context, NanoHTTPD.IHTTPSession session, OrchidRoute route, Map<String, String> files) {
        this.context = context;
        this.session = session;
        this.route = route;
        this.files = files;
        this.body = new HashMap<>();

        String contentType = "";
        String contentTypeHeader = session.getHeaders().get("content-type");

        StringTokenizer st = null;
        if (contentTypeHeader != null) {
            st = new StringTokenizer(contentTypeHeader, ",; ");
            if (st.hasMoreTokens()) {
                contentType = st.nextToken();
            }
        }

        if(session.getMethod() == NanoHTTPD.Method.PUT && files.containsKey("content")) {
            File file = new File(files.get("content"));
            if(file.exists()) {
                try {
                    String fileContent = IOUtils.toString(new FileInputStream(file), Charset.forName("UTF-8"));

                    if(contentType.equalsIgnoreCase("application/json")) {
                        JSONObject body = new JSONObject(fileContent);
                        for(String key : body.keySet()) {
                            this.body.put(key, body.get(key));
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if(session.getMethod() == NanoHTTPD.Method.POST && files.containsKey("postData")) {
            if(contentType.equalsIgnoreCase("application/json")) {
                JSONObject body = new JSONObject(files.get("postData"));
                for(String key : body.keySet()) {
                    this.body.put(key, body.get(key));
                }
            }

        }

        pathParams = ServerUtils.parsePathParams(
                OrchidUtils.normalizePath(route.getPath()),
                OrchidUtils.normalizePath(session.getUri())
        );
    }

    public String path(String key) {
        return pathParams.get(key);
    }

    public String query(String key) {
        return (session.getParameters().get(key).size() > 0) ? session.getParameters().get(key).get(0) : "";
    }

    public List<String> queryList(String key) {
        return session.getParameters().get(key);
    }

    public Map<String, String> files() {
        return files;
    }

    public String input(String key) {
        return all().get(key).toString();
    }

    public Map<String, Object> all() {
        Map<String, Object> all = new HashMap<>();
        all.putAll(pathParams);
        for(Map.Entry<String, List<String>> entry : session.getParameters().entrySet()) {
            if(entry.getValue().size() > 0) {
                all.put(entry.getKey(), entry.getValue().get(0));
            }
            else {
                all.put(entry.getKey(), "");
            }
        }
        all.putAll(files);
        all.putAll(body);

        return all;
    }

    public JSONObject body() {
        return new JSONObject();
    }

    public OrchidContext getContext() {
        return context;
    }
}
