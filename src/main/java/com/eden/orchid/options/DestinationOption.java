package com.eden.orchid.options;

import com.caseyjbrooks.clog.Clog;

public class DestinationOption extends SiteOption {
    @Override
    public String getFlag() {
        return "d";
    }

    @Override
    public boolean parseOption(String[] options) {
        if(options.length == 2) {
            SiteOptions.siteOptions.put("outputDir", options[1]);
            SiteOptions.outputDir = options[1];
            return true;
        }
        else {
            Clog.e("'-d' option should be of length 2: given #{$1}", new Object[] {options});
            return false;
        }
    }

    @Override
    public void setDefault() {
        throw new AssertionError("The outputDir MUST be set with the '-d' flag.");
    }
}
