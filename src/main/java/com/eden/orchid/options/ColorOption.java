package com.eden.orchid.options;

import org.json.JSONArray;

public class ColorOption extends SiteOption {
    @Override
    public String getFlag() {
        return "colors";
    }

    @Override
    public boolean parseOption(String[] options) {
        SiteOptions.siteOptions.put("colors", new JSONArray(options[1].split("\\s+")));
        return true;
    }

    @Override
    public void setDefault() {
        SiteOptions.siteOptions.put("colors", new JSONArray(new String[] {"#2a623d", "#000000"}));
    }
}
