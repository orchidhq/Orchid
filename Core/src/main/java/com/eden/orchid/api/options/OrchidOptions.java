package com.eden.orchid.api.options;


import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.orchid.Orchid;
import com.eden.orchid.utilities.ObservableTreeSet;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
public final class OrchidOptions {

    private List<OrchidOption> missingRequiredOptions = new ArrayList<>();
    private Set<OrchidOption> options;

    @Inject
    public OrchidOptions() {
        this.options = new ObservableTreeSet<>(Orchid.findOptions());
    }

    public void parseOptions(Map<String, String[]> optionsMap, JSONObject siteOptions) {

        // If an option is found and it was successfully parsed, continue to the next parser. Otherwise set its default value.
        for (OrchidOption option : options) {

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
}
