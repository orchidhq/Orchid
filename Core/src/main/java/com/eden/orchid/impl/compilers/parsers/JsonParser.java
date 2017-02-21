package com.eden.orchid.impl.compilers.parsers;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.compilers.OrchidParser;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParser extends OrchidParser {
    @Override
    public String[] getSourceExtensions() {
        return new String[] {"json"};
    }

    @Override
    public JSONElement parse(String extension, String input) {
        try {
            return new JSONElement(new JSONObject(input));
        }
        catch (Exception e) {}
        try {
            return new JSONElement(new JSONArray(input));
        }
        catch (Exception e) {
        }

        return null;
    }
}
