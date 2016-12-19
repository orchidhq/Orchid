package com.eden.orchid.options;

import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class SiteOptions {

    public static Set<SiteOption> optionsParsers = new HashSet<>();

    public static JSONObject siteOptions = new JSONObject();

    public static void startDiscovery(RootDoc root, JSONObject siteOptions) {
        String[][] options = root.options();

        // parses all the options flags according to the parsers set up in SiteOptions.optionsParsers
        for (String[] a : options) {
            for(SiteOption option : optionsParsers) {
                if (a[0].equals("-" + option.getFlag())) {
                    if(option.parseOption(a)) {
                        option.wasFound = true;
                    }
                    break;
                }
            }
        }

        // If any option wasn't found, allow it to set its own default value
        for(SiteOption option : optionsParsers) {
            if (!option.wasFound) {
                option.setDefault();
            }
        }
    }

    public static int optionLength(String option) {
        int result = Standard.optionLength(option);
        if (result != 0) { return result; }
        else { return 2; }
    }
}
