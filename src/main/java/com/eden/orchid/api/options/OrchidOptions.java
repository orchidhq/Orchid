package com.eden.orchid.api.options;


import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.orchid.api.registration.AutoRegister;
import com.eden.orchid.api.registration.Contextual;
import com.sun.tools.doclets.standard.Standard;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AutoRegister
public class OrchidOptions implements Contextual {

    private List<OrchidOption> missingRequiredOptions = new ArrayList<>();

    public void parseOptions(Map<String, String[]> optionsMap, JSONObject siteOptions) {

        // If an option is found and it was successfully parsed, continue to the next parser. Otherwise set its default value.
        for (OrchidOption option : getRegistrar().resolveSet(OrchidOption.class)) {

            if (optionsMap.containsKey("-" + option.getFlag()) || option.optionLength() == 0) {
                String[] optionStrings = (optionsMap.containsKey("-" + option.getFlag()))
                        ? optionsMap.get("-" + option.getFlag())
                        : new String[0];

                if (optionStrings.length > 0 && optionStrings.length != option.optionLength()) {
                    Clog.e("'-#{$1}' option should be of length #{$2}: was given #{$3}", new Object[]{option.getFlag(), option.optionLength(), optionStrings.length});
                    continue;
                }
                else {
                    JSONElement optionValue = option.parseOption(optionStrings);

                    if (optionValue == null) {
                        if (option.isRequired()) {
                            missingRequiredOptions.add(option);
                        }
                        else {
                            optionValue = option.getDefaultValue();
                        }
                    }

                    if (optionValue != null) {
                        siteOptions.put(option.getFlag(), optionValue.getElement());
                    }
                }
            }
            else {

                if (option.isRequired()) {
                    missingRequiredOptions.add(option);
                }
                else {
                    JSONElement optionValue = option.getDefaultValue();
                    if (optionValue != null) {
                        siteOptions.put(option.getFlag(), optionValue.getElement());
                    }
                }
            }
        }
    }

    public boolean shouldContinue() {
        if (missingRequiredOptions.size() > 0) {
            for (OrchidOption option : missingRequiredOptions) {
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
    public int optionLength(String optionFlag) {
        // checking option length occurs before the bootstrap() method is run, so we must manually register Options here
        // so they can vetted by the Javadoc tool
        if (getRegistrar().resolveSet(OrchidOption.class).size() == 0) {
            FastClasspathScanner scanner = new FastClasspathScanner();
            scanner.matchClassesWithAnnotation(AutoRegister.class, (matchingClass) -> {
                //TODO: Fix this
//                try {
//                    if (OrchidOption.class.isAssignableFrom(matchingClass)) {
//                        OrchidOption option = (OrchidOption) matchingClass.newInstance();
//                        int priority = option.priority();
//                        while (getRegistrar().resolveSet(OrchidOption.class).containsKey(priority)) {
//                            priority--;
//                        }
//
//                        getRegistrar().resolveSet(OrchidOption.class).put(priority, option);
//                    }
//                }
//                catch (IllegalAccessException | InstantiationException e) {
//                    e.printStackTrace();
//                }
            });
            scanner.scan();
        }

        for (OrchidOption option : getRegistrar().resolveSet(OrchidOption.class)) {
            if (optionFlag.equals("-" + option.getFlag())) {
                return option.optionLength();
            }
        }

        return Standard.optionLength(optionFlag);
    }
}
