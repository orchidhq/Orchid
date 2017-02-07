package com.eden.orchid.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.RegistrationProvider;
import com.sun.tools.doclets.standard.Standard;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@AutoRegister
public class SiteOptions implements RegistrationProvider {

    public static Map<Integer, Option> optionsParsers = new TreeMap<>(Collections.reverseOrder());
    private static List<Option> missingRequiredOptions = new ArrayList<>();

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
                        if (optionParser.isRequired()) {
                            missingRequiredOptions.add(optionParser);
                        }
                        else {
                            optionValue = optionParser.getDefaultValue();
                        }
                    }

                    if (optionValue != null) {
                        siteOptions.put(optionParser.getFlag(), optionValue.getElement());
                    }
                }
            }
            else {

                if (optionParser.isRequired()) {
                    missingRequiredOptions.add(optionParser);
                }
                else {
                    JSONElement optionValue = optionParser.getDefaultValue();
                    if (optionValue != null) {
                        siteOptions.put(optionParser.getFlag(), optionValue.getElement());
                    }
                }
            }
        }
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
        if (missingRequiredOptions.size() > 0) {
            for (Option option : missingRequiredOptions) {
                Clog.e("Missing required option: -#{$1} - #{$2}", new Object[]{option.getFlag(), option.getDescription()});
            }
            return false;
        }
        else {
            return true;
        }
    }

// Handle Javadoc-specific stuff
//----------------------------------------------------------------------------------------------------------------------
    public static int optionLength(String optionFlag) {
        // checking option length occurs before the bootstrap() method is run, so we must manually register Options here
        // so they can vetted by the Javadoc tool
        if (optionsParsers.size() == 0) {
            FastClasspathScanner scanner = new FastClasspathScanner();
            scanner.matchClassesWithAnnotation(AutoRegister.class, (matchingClass) -> {
                try {
                    if (Option.class.isAssignableFrom(matchingClass)) {
                        Option option = (Option) matchingClass.newInstance();
                        int priority = option.priority();
                        while (optionsParsers.containsKey(priority)) {
                            priority--;
                        }

                        SiteOptions.optionsParsers.put(priority, option);
                    }
                }
                catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            });
            scanner.scan();
        }

        for (Map.Entry<Integer, Option> option : optionsParsers.entrySet()) {
            if (optionFlag.equals("-" + option.getValue().getFlag())) {
                return option.getValue().optionLength();
            }
        }

        return Standard.optionLength(optionFlag);
    }
}
