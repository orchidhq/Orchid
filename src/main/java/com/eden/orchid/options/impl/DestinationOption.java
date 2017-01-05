package com.eden.orchid.options.impl;

import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.JSONElement;
import com.eden.orchid.options.Option;

@AutoRegister
public class DestinationOption implements Option {

    @Override
    public String getFlag() {
        return "d";
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
        return 100;
    }

    @Override
    public int optionLength() {
        return 2;
    }
}
