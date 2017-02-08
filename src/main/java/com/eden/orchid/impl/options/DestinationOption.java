package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.options.OrchidOption;
import com.eden.orchid.api.registration.AutoRegister;

@AutoRegister
public class DestinationOption implements OrchidOption {

    @Override
    public String getFlag() {
        return "d";
    }

    @Override
    public String getDescription() {
        return "the output directory for all generated files";
    }

    @Override
    public JSONElement parseOption(String[] options) {
        return new JSONElement(options[1]);
    }

    @Override
    public JSONElement getDefaultValue() {
        return null;
    }

    @Override
    public int priority() {
        return 1000;
    }

    @Override
    public int optionLength() {
        return 2;
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
