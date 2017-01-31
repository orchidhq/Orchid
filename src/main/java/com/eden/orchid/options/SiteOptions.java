package com.eden.orchid.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.orchid.utilities.RegistrationProvider;
import com.sun.tools.doclets.standard.Standard;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class SiteOptions implements RegistrationProvider {

    public static Map<Integer, Option> optionsParsers = new TreeMap<>(Collections.reverseOrder());

    public static void parseOptions(Map<String, String[]> optionsMap, JSONObject siteOptions) {
        // If an option is found and it was successfully parsed, continue to the next parser. Otherwise set its default value.
        for (Map.Entry<Integer, Option> option : optionsParsers.entrySet()) {
            Option optionParser = option.getValue();

            if (optionsMap.containsKey("-" + option.getValue().getFlag()) || option.getValue().optionLength() == 0) {
                String[] optionStrings = (optionsMap.containsKey("-" + option.getValue().getFlag()))
                        ? optionsMap.get("-" + optionParser.getFlag())
                        : new String[0];

                if (optionStrings.length > 0 && optionStrings.length != optionParser.optionLength()) {
                    Clog.e("'-#{$1}' option should be of length #{$2}: was given #{$3}", new Object[]{optionParser.getFlag(), optionParser.optionLength(), optionStrings.length});
                    continue;
                }
                else {
                    JSONElement optionValue = optionParser.parseOption(optionStrings);

                    if (optionValue == null) {
                        optionValue = optionParser.getDefaultValue();
                    }

                    if (optionValue != null) {
                        siteOptions.put(optionParser.getFlag(), optionValue.getElement());
                    }
                }
            }
        }
    }

    public static int optionLength(String optionFlag) {
        // if the option is a standard option, return its length
        int result = Standard.optionLength(optionFlag);
        if (result != 0) {
            return result;
        }

        // Otherwise, find the matching registered Option and return its specified option length
        else {
            for (Map.Entry<Integer, Option> option : optionsParsers.entrySet()) {
                Clog.d("Checking option length: #{$1} - [#{$2}, #{$3}", new Object[]{optionFlag, "-" + option.getValue().getFlag(), option.getValue().optionLength()});
                if (optionFlag.equals("-" + option.getValue().getFlag())) {
                    return option.getValue().optionLength();
                }
            }
        }

        // Failing that, return 2 because we can't find an appropriate option to match, but we don't necessarily need to crash
        return 2;
    }

    @Override
    public void register(Object object) {
        if (object instanceof Option) {
            Option option = (Option) object;
            int priority = option.priority();
            while (optionsParsers.containsKey(priority)) {
                priority--;
            }

            SiteOptions.optionsParsers.put(priority, option);
        }
    }

    public static boolean shouldContinue() {
        return true;
    }
}
