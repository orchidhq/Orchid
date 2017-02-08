package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.options.OrchidOption;
import com.eden.orchid.api.registration.AutoRegister;

@AutoRegister
public class WikiPathOption implements OrchidOption {

    public static String path;

    @Override
    public String getFlag() {
        return "wikiPath";
    }

    @Override
    public String getDescription() {
        return "The path used for your wiki. Includes both the path for your source wiki files, and also the output path.";
    }

    @Override
    public JSONElement parseOption(String[] options) {
        path = options[1];

        if (!options[1].endsWith("/")) {
            path += "/";
        }

        return new JSONElement(options[1]);
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public JSONElement getDefaultValue() {
        return null;
    }

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public int optionLength() {
        return 2;
    }
}
