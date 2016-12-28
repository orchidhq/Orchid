package com.eden.orchid.options.impl;

import com.eden.orchid.AutoRegister;
import com.eden.orchid.JSONElement;
import com.eden.orchid.options.Option;

@AutoRegister
public class BaseUrlOption implements Option {

    @Override
    public String getFlag() {
        return "baseUrl";
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
        return 60;
    }

    @Override
    public int optionLength() {
        return 2;
    }
}
