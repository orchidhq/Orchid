package com.eden.orchid.compilers;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ContentCompiler {
    String compile(String input, JSONObject json);
    String compile(String input, JSONArray json);

    int priority();
}