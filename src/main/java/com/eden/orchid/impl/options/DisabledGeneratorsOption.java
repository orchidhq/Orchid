package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.options.Option;
import com.eden.orchid.utilities.AutoRegister;
import org.json.JSONArray;

@AutoRegister
public class DisabledGeneratorsOption implements Option {

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
        String[] classNames = options[1].split(":");
        JSONArray classNamesArray = new JSONArray();

        for(String className : classNames) {
            classNamesArray.put(className);
        }

        return new JSONElement(classNames);
    }

    @Override
    public JSONElement getDefaultValue() {
        return null;
    }

    @Override
    public int priority() {
        return 40;
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
