package com.eden.orchid.api.server;

import com.eden.orchid.utilities.OrchidUtils;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class OrchidRequest {

    private OrchidRoute route;
    private NanoHTTPD.IHTTPSession session;

    private Map<String, String> pathParams;
    private Map<String, String> files;

    public OrchidRequest(NanoHTTPD.IHTTPSession session, OrchidRoute route, Map<String, String> files) {
        this.session = session;
        this.route = route;
        this.files = files;

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
        if(pathParams.containsKey(key)) {
            return pathParams.get(key);
        }
        else if(session.getParameters().containsKey(key)) {
            return (session.getParameters().get(key).size() > 0) ? session.getParameters().get(key).get(0) : "";
        }
        else if(files.containsKey(key)) {
            return files.get(key);
        }
        return null;
    }

    public JSONObject all() {
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

        return new JSONObject(all);
    }

    public JSONObject body() {
        return new JSONObject();
    }
}
