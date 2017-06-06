package com.eden.orchid.impl.compilers.parsers;

import com.eden.orchid.api.compilers.OrchidParser;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParser extends OrchidParser {

    @Override
    public String[] getSourceExtensions() {
        return new String[] {"json"};
    }

    @Override
    public JSONObject parse(String extension, String input) {
        // first try parsing it as JSON Object
        try {
            return new JSONObject(input);
        }
        catch (Exception e) {}

        // If it fails to parse as JSON Object and throws exception, try again as JSON Array
        try {
            JSONObject object = new JSONObject();
            object.put(OrchidParser.arrayAsObjectKey, new JSONArray(input));
            return object;
        }
        catch (Exception e) {
        }

        return null;
    }
}
