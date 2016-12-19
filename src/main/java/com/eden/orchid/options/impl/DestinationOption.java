package com.eden.orchid.options.impl;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.AutoRegister;
import com.eden.orchid.options.SiteOption;
import com.eden.orchid.options.SiteOptions;

@AutoRegister
public class DestinationOption extends SiteOption {

    @Override
    public String getFlag() {
        return "d";
    }

    @Override
    public boolean parseOption(String[] options) {
        if(options.length == 2) {
            SiteOptions.siteOptions.put("outputDir", options[1]);
            return true;
        }
        else {
            Clog.e("'-d' option should be of length 2: given #{$1}", new Object[] {options});
            return false;
        }
    }

    @Override
    public void setDefault() {

    }
}
