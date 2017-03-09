package com.eden.orchid.impl.events;

import com.eden.common.json.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.events.On;
import com.eden.orchid.utilities.OrchidUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.IOException;
import java.util.EventListener;

public class LoadExternalIndices implements EventListener {

    private OrchidContext context;
    private OkHttpClient client;

    @Inject
    public LoadExternalIndices(OrchidContext context) {
        this.context = context;
        this.client = new OkHttpClient();
    }

    @On(Orchid.Events.DATAFILES_PARSED)
    public void onDatafilesParsed(JSONObject options) {
        if(options != null) {
            JSONElement externalIndexList = new JSONElement(options).query("data.externalIndex");

            if(OrchidUtils.elementIsObject(externalIndexList)) {
                JSONObject externalIndex = (JSONObject) externalIndexList.getElement();

                if(externalIndex.has("links") && externalIndex.get("links") instanceof JSONArray) {
                    loadIndices(options, externalIndex.getJSONArray("links"));
                }
            }
            else if(OrchidUtils.elementIsArray(externalIndexList)) {
                JSONArray externalIndex = (JSONArray) externalIndexList.getElement();

                loadIndices(options, externalIndex);
            }
        }
    }

    private void loadIndices(JSONObject options, JSONArray refersTo) {
        JSONArray fullIndex = new JSONArray();
        JSONObject keyedIndex = new JSONObject();

        for (int i = 0; i < refersTo.length(); i++) {
            JSONObject index = loadAdditionalFile(refersTo.optString(i));

            if(index != null) {
                fullIndex.put(index);

                for(String key : index.keySet()) {
                    keyedIndex.putOnce(key, new JSONArray());

                    if(index.get(key) instanceof JSONArray) {
                        JSONArray array = index.getJSONArray(key);

                        for (int j = 0; j < array.length(); j++) {
                            keyedIndex.getJSONArray(key).put(array.get(j));
                        }
                    }
                }
            }
        }

        options.put("externalIndex", new JSONObject());
        options.getJSONObject("externalIndex").put("fullIndex", fullIndex);
        options.getJSONObject("externalIndex").put("keyedIndex", keyedIndex);
    }

    private JSONObject loadAdditionalFile(String url) {
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();

            if(response.isSuccessful()) {
                return new JSONObject(response.body().string());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
