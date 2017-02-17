package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.options.OrchidOption;
import org.json.JSONArray;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;

@Singleton
public class DisabledGeneratorsOption extends OrchidOption {

    @Inject
    public DisabledGeneratorsOption() {
        this.priority = 500;
    }

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
        if (options[1].contains(File.pathSeparator)) {
            classNames = options[1].split(File.pathSeparator);
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
    public int optionLength() {
        return 2;
    }

    @Override
    public boolean isRequired() {
        return false;
    }
}
