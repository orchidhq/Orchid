package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.options.OrchidOption;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VersionOption extends OrchidOption {

    @Inject
    public VersionOption() {
        this.priority = 400;
    }

    @Override
    public String getFlag() {
        return "v";
    }

    @Override
    public String getDescription() {
        return "the version of your library";
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
    public int optionLength() {
        return 2;
    }

    @Override
    public boolean isRequired() {
        return false;
    }
}
