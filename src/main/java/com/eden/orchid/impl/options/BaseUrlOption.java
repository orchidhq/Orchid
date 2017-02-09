package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.options.OrchidOption;

import javax.inject.Singleton;

@Singleton
public class BaseUrlOption implements OrchidOption {

    @Override
    public String getFlag() {
        return "baseUrl";
    }

    @Override
    public String getDescription() {
        return "the base URL to append to generated URLs.";
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
        return 700;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public int optionLength() {
        return 2;
    }
}
