package com.eden.orchid.options;

import org.json.JSONObject;

public interface Option {
    String getFlag();
    boolean parseOption(JSONObject siteOptions, String[] options);
    void setDefault(JSONObject siteOptions);
    int priority();
}
