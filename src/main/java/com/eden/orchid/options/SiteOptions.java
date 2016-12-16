package com.eden.orchid.options;

import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class SiteOptions {

    public static Set<SiteOption> optionsParsers = new HashSet<>();

    public static JSONObject siteOptions;
    public static String outputDir;

    public static JSONObject startDiscovery(RootDoc root) {
        if(siteOptions == null) {
            siteOptions = new JSONObject();
            initializeOptions(root);
        }

        return siteOptions;
    }

    private static void initializeOptions(RootDoc root) {
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

        // Each
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
