package com.eden.orchid.options.impl;

import com.eden.common.json.JSONElement;
import com.eden.orchid.options.Option;
import com.eden.orchid.utilities.AutoRegister;

@AutoRegister
public class VersionOption implements Option {

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
    public int priority() {
        return 70;
    }

    @Override
    public int optionLength() {
        return 2;
    }
}
