package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.options.OrchidOption;
import com.eden.orchid.api.registration.AutoRegister;
import org.json.JSONArray;

@AutoRegister
public class DisabledGeneratorsOption implements OrchidOption {

    @Override
    public String getFlag() {
        return "disabledGenerators";
    }

    @Override
    public String getDescription() {
        return "Selectively turn off the usage of generators. This should be a list of Class names (fully-qualified " +
                "names are optional) separated by colons (:) with no spaces. Can be specified in your config.yml as a " +
                "normal array of strings.";
    }

    @Override
    public JSONElement parseOption(String[] options) {
        String[] classNames;
        if (options[1].contains(":")) {
            classNames = options[1].split(":");
        }
        else {
            classNames = new String[]{options[1]};
        }

        JSONArray classNamesArray = new JSONArray();
        for (String className : classNames) {
            classNamesArray.put(className);
        }

        if(classNamesArray.length() > 0) {
            return new JSONElement(classNamesArray);
        }
        else {
            return null;
        }
    }

    @Override
    public JSONElement getDefaultValue() {
        return null;
    }

    @Override
    public int priority() {
        return 500;
    }

    @Override
    public int optionLength() {
        return 2;
    }

    @Override
    public boolean isRequired() {
        return false;
    }
}
