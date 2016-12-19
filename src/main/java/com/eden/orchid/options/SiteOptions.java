package com.eden.orchid.options;

import com.caseyjbrooks.clog.Clog;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SiteOptions {

    public static Map<Integer, Option> optionsParsers = new TreeMap<>();

    public static void startDiscovery(RootDoc root, JSONObject siteOptions) {
        String[][] options = root.options();

        Map<Option, Boolean> optionSuccesses = new HashMap<>();

        // parses all the options flags according to the parsers set up in SiteOptions.optionsParsers
        for (String[] a : options) {
            for(Map.Entry<Integer, Option> option : optionsParsers.entrySet()) {

                Clog.d("Parsing option: #{$1}:[#{$2 | className}]", option.getKey(), option.getValue());

                if (a[0].equals("-" + option.getValue().getFlag())) {
                    if(option.getValue().parseOption(siteOptions, a)) {
                        optionSuccesses.put(option.getValue(), true);
                    }
                    else {
                        optionSuccesses.put(option.getValue(), false);
                    }
                    break;
                }
            }
        }

        // If any option wasn't found, allow it to set its own default value
        for(Map.Entry<Option, Boolean> option : optionSuccesses.entrySet()) {
            if (!option.getValue()) {
                option.getKey().setDefault(siteOptions);
            }
        }
    }

    public static int optionLength(String option) {
        int result = Standard.optionLength(option);
        if (result != 0) { return result; }
        else { return 2; }
    }
}
