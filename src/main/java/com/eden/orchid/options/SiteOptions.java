package com.eden.orchid.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.JSONElement;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.standard.Standard;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SiteOptions {

    public static Map<Integer, Option> optionsParsers = new TreeMap<>(Collections.reverseOrder());

    public static void startDiscovery(RootDoc root, JSONObject siteOptions) {
        String[][] options = root.options();

        Map<String, String[]> optionsMap = new HashMap<>();

        // Adds options to a map so they can be more effectively parsed
        for (String[] a : options) {
            optionsMap.put(a[0], a);
        }

        // If an option is found and it was successfully parsed, continue to the next parser. Otherwise set its default value.
        for(Map.Entry<Integer, Option> option : optionsParsers.entrySet()) {
            Clog.d("Parsing option: #{$1}:[#{$2 | className}]", option.getKey(), option.getValue());
            Option optionParser = option.getValue();

            String[] optionStrings = (optionsMap.containsKey("-" + option.getValue().getFlag()))
                    ? optionsMap.get("-" + optionParser.getFlag())
                    : new String[] {};

            JSONElement optionValue = optionParser.parseOption(optionStrings);

            if(optionValue == null) {
                optionValue = optionParser.getDefaultValue();
            }

            if(optionValue != null) {
                siteOptions.put(optionParser.getFlag(), optionValue.getElement());
            }
        }
    }

    public static int optionLength(String option) {
        int result = Standard.optionLength(option);
        if (result != 0) { return result; }
        else { return 2; }
    }
}
