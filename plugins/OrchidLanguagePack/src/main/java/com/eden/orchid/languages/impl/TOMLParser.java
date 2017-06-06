package com.eden.orchid.languages.impl;

import com.eden.orchid.api.compilers.OrchidParser;
import com.moandjiezana.toml.Toml;
import org.json.JSONObject;

public class TOMLParser extends OrchidParser {

    @Override
    public String[] getSourceExtensions() {
        return new String[] {"tml", "toml"};
    }

    @Override
    public JSONObject parse(String extension, String input) {
        Toml toml = new Toml().read(input);
        JSONObject object = new JSONObject(toml.toMap());
        return object;
    }
}
