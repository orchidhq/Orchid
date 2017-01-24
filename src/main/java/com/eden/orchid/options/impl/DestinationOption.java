package com.eden.orchid.options.impl;

import com.eden.common.json.JSONElement;
import com.eden.orchid.options.Option;
import com.eden.orchid.utilities.AutoRegister;

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
