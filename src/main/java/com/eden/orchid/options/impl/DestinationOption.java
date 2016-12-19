package com.eden.orchid.options.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.AutoRegister;
import com.eden.orchid.options.Option;
import org.json.JSONObject;

@AutoRegister
public class DestinationOption implements Option {

    @Override
    public String getFlag() {
        return "d";
    }

    @Override
    public boolean parseOption(JSONObject siteOptions, String[] options) {
        if(options.length == 2) {
            siteOptions.put("outputDir", options[1]);
            return true;
        }
        else {
            Clog.e("'-d' option should be of length 2: given #{$1}", new Object[] {options});
            return false;
        }
    }

    @Override
    public void setDefault(JSONObject siteOptions) {

    }

    @Override
    public int priority() {
        return 100;
    }
}
